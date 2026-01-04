package dev.faridg.ansibling.presentation.execute_ci

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.execution.ExecutionEntity
import dev.faridg.ansibling.data.room.entity.execution.ExecutionOutputEntity
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.data.ssh.SSHExecutor
import dev.faridg.ansibling.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ExecutePlaybookViewModel(
    private val playbook: Playbook
) {
    private val executionDao = AppDatabase.playbookExecutionDao
    private val deviceDao = AppDatabase.deviceDao
    private val variableDao = AppDatabase.variableDao
    val execution: MutableStateFlow<ExecutionEntity?> = MutableStateFlow(null)
    val outputs: SnapshotStateMap<DeviceGroup?, SnapshotStateMap<Device?, SnapshotStateList<ExecutionOutputEntity>>> =
        SnapshotStateMap()
    val progress: Flow<Float> = flowOf(0F)
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var executionId = -1L

    init {
        // Create a new execution and start.
        viewModelScope.launch {
            executionId = getExecutionId()
            observeExecutionStatus()
            startExecution()
        }
    }

    fun startExecution() {
        viewModelScope.launch {
            val variables = loadVariables()
            println("Executing playbook: $playbook")

            try {
                playbook.devices.forEach { target ->
                    when (target) {
                        is Device -> {
                            playbook.executeForDevice(
                                device = target,
                                group = null,
                                variables = variables
                            )
                        }

                        is DeviceGroup -> {
                            val devices = deviceDao.getAllDevicesForGroup(target.id).devices

                            devices.forEach { entity ->
                                playbook.executeForDevice(
                                    device = entity.toDomain(),
                                    group = target,
                                    variables = variables
                                )
                            }
                        }
                    }
                }

                updateExecutionStatus(TaskStatusType.SUCCESS)
            } catch (e: Exception) {
                updateExecutionStatus(TaskStatusType.ERROR)

                insertExecutionOutput(
                    type = StatusType.ERROR,
                    output = e.message ?: "Unknown error",
                    device = null,
                    group = null
                )
            }
        }
    }

    suspend fun Playbook.executeForDevice(
        device: Device,
        group: DeviceGroup?,
        variables: MutableMap<String, String>
    ) {
        try {
            val executor = SSHExecutor(device = device)

            insertExecutionOutput(
                type = StatusType.INFO,
                output = "• Auth success for device: [${device.nickName}/${device.ip}]",
                group = group,
                device = device
            )

            actions.forEach { action ->
                insertExecutionOutput(
                    type = StatusType.INFO,
                    output = "▶ Executing: ${action.title.ifBlank { "Script #${action.order}" }}",
                    group = group,
                    device = device
                )
                // Execute command
                try {
                    // Render jinja template in case there is any.
                    val command = when (action.type) {
                        ScriptType.PLAIN -> action.content
                        ScriptType.JINJA -> action.content.renderJinja(variables = variables)
                    }
                    executor.executeCommand(command) { line, type ->
                        insertExecutionOutput(
                            type = type,
                            output = line,
                            group = group,
                            device = device
                        )
                    }
                } catch (e: Exception) {
                    when (action.exceptionBehaviour) {
                        ExceptionBehavior.IGNORE -> insertExecutionOutput(
                            type = StatusType.WARNING,
                            output = e.message ?: "Unknown error",
                            group = group,
                            device = device
                        )

                        ExceptionBehavior.FAIL -> throw e // Re-throw the exception
                    }
                }
            }

            executor.close()
        } catch (e: Exception) {
            insertExecutionOutput(
                type = StatusType.ERROR,
                output = e.message ?: "Unknown error",
                group = group,
                device = device
            )
        }
    }

    private fun insertExecutionOutput(
        type: StatusType,
        output: String,
        group: DeviceGroup?,
        device: Device?
    ) {
        val groupDevices = outputs.getOrPut(group) { SnapshotStateMap() }
        val deviceOutputs = groupDevices.getOrPut(device) { SnapshotStateList() }
        deviceOutputs.add(
            ExecutionOutputEntity(
                executionId = executionId,
                output = output,
                type = type
            )
        )
    }

    private suspend fun updateExecutionStatus(
        status: TaskStatusType
    ) {
        executionDao.insertExecution(
            ExecutionEntity(
                executionId = executionId,
                playbookId = playbook.id,
                status = status
            )
        )
    }

    private suspend fun loadVariables(): MutableMap<String, String> {
        val vars = mutableMapOf<String, String>()
        variableDao.observeAll().first()
            .map { it.toDomain() }
            .forEach { vars[it.key] = it.value }
        return vars
    }

    private suspend fun loadScripts(): Map<String, Script> {
        return AppDatabase.scriptDao.observeAll().first()
            .map { it.toDomain() }.associateBy { it.scriptId }
    }

    private suspend fun getExecutionId(): Long {
        return AppDatabase.playbookExecutionDao.insertExecution(
            ExecutionEntity(
                playbookId = playbook.id,
                status = TaskStatusType.IN_PROGRESS
            )
        )
    }

    private fun observeExecutionStatus() {
        viewModelScope.launch {
            AppDatabase.playbookExecutionDao.observeExecution(executionId).collectLatest { newExecution ->
                execution.update { newExecution }
            }
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }
}
