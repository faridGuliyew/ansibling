package dev.faridg.ansibling.presentation.execute_ci

import dev.faridg.ansibling.data.room.AppDatabase
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
    val execution : MutableStateFlow<ExecutionEntity?> = MutableStateFlow(null)
    val outputs : MutableStateFlow<List<ExecutionOutputEntity>> = MutableStateFlow(emptyList())
    val progress: Flow<Float> = flowOf(0F)
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var executionId = -1L

    init {
        // Create a new execution and start.
        viewModelScope.launch {
            executionId = getExecutionId()
            observeExecutionStatus()
            observeExecutionOutputs()
            startExecution()
        }
    }

    fun startExecution() {
        viewModelScope.launch {
            val variables = loadVariables()
            println("Executing playbook: $playbook")
//            val playbook = if (playbook.actions.any { it.globalScriptId != null }) {
//                val scripts = loadScripts()
//                playbook.copy(
//                    actions = playbook.actions.map {
//                        if (it.globalScriptId == null) return@map it
//
//                        it.copy(title = scripts[it.globalScriptId]!!.title, content = scripts[it.globalScriptId]!!.content)
//                    }
//                )
//            } else playbook

            try {
                playbook.devices.forEach { target ->
                    when (target) {
                        is Device -> {
                            playbook.executeForDevice(
                                device = target,
                                variables = variables
                            )
                        }

                        is DeviceGroup -> {
                            val devices = deviceDao.getAllDevicesForGroup(target.id).devices

                            devices.forEach { entity ->
                                playbook.executeForDevice(
                                    device = entity.toDomain(),
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
                    output = e.message ?: "Unknown error"
                )
            }
        }
    }

    suspend fun Playbook.executeForDevice(
        device: Device,
        variables: MutableMap<String, String>
    ) {
        try {
            val executor = SSHExecutor(device = device)

            insertExecutionOutput(
                type = StatusType.INFO,
                output = "• Auth success for device: [${device.nickName}/${device.ip}]"
            )

            actions.forEach { action ->
                insertExecutionOutput(
                    type = StatusType.INFO,
                    output = "▶ Executing: ${action.title.ifBlank { "Script #${action.order}" }}"
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
                            output = line
                        )
                    }
                } catch (e: Exception) {
                    when (action.exceptionBehaviour) {
                        ExceptionBehavior.IGNORE -> insertExecutionOutput(
                            type = StatusType.WARNING,
                            output = e.message ?: "Unknown error"
                        )

                        ExceptionBehavior.FAIL -> throw e // Re-throw the exception
                    }
                }
            }

            executor.close()
        } catch (e: Exception) {
            insertExecutionOutput(
                type = StatusType.ERROR,
                output = e.message ?: "Unknown error"
            )
        }
    }

//    suspend fun executeAction(action: PlaybookAction) {
//        val title = when (action) {
//            is RemoteAction -> action.command.take(100)
//            is Script -> TODO()
//        }
//        insertExecutionOutput(
//            type = StatusType.INFO,
//            output = "▶ Executing: ${action.command.take(100)}"
//        )
//        // Execute command
//        try {
//            // Render jinja template in case there is any.
//            val command = when (action.commandType) {
//                RemoteActionCommandType.PLAIN -> action.command
//                RemoteActionCommandType.JINJA -> action.command.renderJinja(variables = variables)
//            }
//            executor.executeCommand(command) { line, type ->
//                insertExecutionOutput(
//                    type = type,
//                    output = line
//                )
//            }
//        } catch (e: Exception) {
//            when (action.exceptionBehaviour) {
//                ExceptionBehavior.IGNORE -> insertExecutionOutput(
//                    type = StatusType.WARNING,
//                    output = e.message ?: "Unknown error"
//                )
//
//                ExceptionBehavior.FAIL -> throw e // Re-throw the exception
//            }
//        }
//    }

    private suspend fun insertExecutionOutput(
        type: StatusType,
        output: String
    ) {
        executionDao.insertExecutionOutput(
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

    private suspend fun getExecutionId() : Long {
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
    private fun observeExecutionOutputs() {
        viewModelScope.launch {
            AppDatabase.playbookExecutionDao.observeOutputs(executionId).collectLatest { newOutputs ->
                outputs.update { newOutputs }
            }
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }
}
