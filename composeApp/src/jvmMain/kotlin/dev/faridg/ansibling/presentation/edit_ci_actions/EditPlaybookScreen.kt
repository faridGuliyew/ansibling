package dev.faridg.ansibling.presentation.edit_ci_actions

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_add
import ansibling.composeapp.generated.resources.ic_more_vert
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookDeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookEntity
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.data.room.toEntity
import dev.faridg.ansibling.domain.*
import dev.faridg.ansibling.ui_kit.ExpandableContent
import dev.faridg.ansibling.ui_kit.device.DeviceItem
import dev.faridg.ansibling.ui_kit.device_group.DeviceGroupItem
import dev.faridg.ansibling.ui_kit.script.ScriptItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.util.*


@Composable
fun EditPlaybookScreen(
    playbook: Playbook,
    onExit: () -> Unit
) {
    val uiModel = remember { playbook.toUiModel() }
    val allDevices = remember { mutableStateListOf<PlaybookDevice>() }
    val allScripts = remember { mutableStateListOf<Script>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val existingIds = uiModel.devices.map {
            when (it) {
                is Device -> it.id
                is DeviceGroup -> it.id
            }
        }.toSet()

        allDevices.addAll(
            AppDatabase.deviceDao.getAllDevicesAsFlow().first()
                .filter { it.deviceId !in existingIds }
                .map { it.toDomain() }
        )
        allDevices.addAll(
            AppDatabase.deviceDao.getAllDeviceGroupsWithDevicesAsFlow().first()
                .filter { it.deviceGroup.groupId !in existingIds }
                .map { it.toDomain() }
        )

    }
    LaunchedEffect(Unit) {
        AppDatabase.scriptDao.observeAll().collectLatest {
            allScripts.clear()
            allScripts.addAll(it.map { it.toDomain() })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = uiModel.nickname.value,
            onValueChange = { uiModel.nickname.value = it },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )

        // == ADD DEVICE SECTION START ==
        ExpandableContent(
            title = {
                Text(text = "Added devices (${uiModel.devices.size})")
            }
        ) {
            LazyColumn (
                modifier = Modifier.heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(uiModel.devices) { index, it ->
                    when (it) {
                        is Device -> DeviceItem(
                            item = it,
                            onIconClick = {
                                allDevices.add(it)
                                uiModel.devices.removeAt(index)
                            }
                        )
                        is DeviceGroup -> DeviceGroupItem(
                            group = it,
                            onIconClick = {
                                allDevices.add(it)
                                uiModel.devices.removeAt(index)
                            }
                        )
                    }
                }
            }
        }

        ExpandableContent(
            title = {
                Text(text = "All devices")
            }
        ) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(allDevices) { index, it ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        when (it) {
                            is Device -> DeviceItem(
                                item = it,
                                onIconClick = {
                                    uiModel.devices.add(it)
                                    allDevices.removeAt(index)
                                },
                                iconRes = Res.drawable.ic_add
                            )
                            is DeviceGroup -> DeviceGroupItem(
                                group = it,
                                onIconClick = {
                                    uiModel.devices.add(it)
                                    allDevices.removeAt(index)
                                },
                                iconRes = Res.drawable.ic_add
                            )
                        }
                    }
                }
            }
        }
        // == ADD DEVICE SECTION END ==

        LazyColumn(
            modifier = Modifier.heightIn(max = 500.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                uiModel.remoteActions
            ) { index, actionUi ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // ---- main row ----
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = actionUi.command.value,
                            onValueChange = { actionUi.command.value = it },
                            label = { Text("Bash script") },
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                actionUi.expanded.value = !actionUi.expanded.value
                            }
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ic_more_vert),
                                contentDescription = null
                            )
                        }
                    }

                    // ---- expanded panel ----
                    if (actionUi.expanded.value) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            // Exception behavior
                            var dropdownExpanded by remember { mutableStateOf(false) }

                            OutlinedButton(
                                onClick = { dropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Exception behaviour: ${actionUi.exceptionBehavior.value}")
                            }

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                ExceptionBehavior.entries.forEach { behavior ->
                                    DropdownMenuItem(
                                        text = { Text(behavior.name) },
                                        onClick = {
                                            actionUi.exceptionBehavior.value = behavior
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }

                            // Actions row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(
                                        enabled = index > 0,
                                        onClick = {
                                            uiModel.remoteActions.apply {
                                                val prev = this[index - 1]
                                                this[index - 1] = this[index]
                                                this[index] = prev
                                            }
                                        }
                                    ) {
                                        Text("↑")
                                    }

                                    IconButton(
                                        enabled = index < uiModel.remoteActions.lastIndex,
                                        onClick = {
                                            uiModel.remoteActions.apply {
                                                val next = this[index + 1]
                                                this[index + 1] = this[index]
                                                this[index] = next
                                            }
                                        }
                                    ) {
                                        Text("↓")
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = actionUi.commandType.value == RemoteActionCommandType.JINJA,
                                            onCheckedChange = {
                                                actionUi.commandType.let {
                                                    when (it.value) {
                                                        RemoteActionCommandType.PLAIN -> it.value =
                                                            RemoteActionCommandType.JINJA

                                                        RemoteActionCommandType.JINJA -> it.value =
                                                            RemoteActionCommandType.PLAIN
                                                    }
                                                }
                                            }
                                        )
                                        Text("Render as jinja")
                                    }
                                }

                                TextButton(
                                    onClick = {
                                        uiModel.remoteActions.removeAt(index)
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                uiModel.remoteActions.add(
                    RemoteActionUiModel(
                        id = UUID.randomUUID().toString(),
                        playbookId = playbook.id,
                    command = mutableStateOf(""),
                    commandType = mutableStateOf(RemoteActionCommandType.PLAIN),
                    exceptionBehavior = mutableStateOf(ExceptionBehavior.IGNORE),
                    order = (uiModel.remoteActions.maxOfOrNull { it.order } ?: 0) + 1
                )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text("Add script")
        }

        ExpandableContent(
            title = {
                Text(text = "All scripts")
            }
        ) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 500.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allScripts) {
                    ScriptItem(
                        item = it,
                        actionText = "Add",
                        onClick = {}
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    AppDatabase.playbookDao.deleteRemoteActionsForPlaybook(playbook.id)
                    AppDatabase.playbookDao.deletePlaybookDevices(playbook.id)
                    AppDatabase.playbookDao.deletePlaybookDeviceGroups(playbook.id)

                    AppDatabase.playbookDao.insertPlaybook(PlaybookEntity(
                        playbookId = uiModel.id,
                        nickName = uiModel.nickname.value
                    ))
                    AppDatabase.playbookDao.insertRemoteActions(
                        actions = uiModel.remoteActions.map { it.toDomain().toEntity() }
                    )
                    uiModel.devices.forEach {
                        when (it) {
                            is Device -> AppDatabase.playbookDao.insertPlaybookDevice(
                                relation = PlaybookDeviceRelationEntity(it.id, uiModel.id)
                            )
                            is DeviceGroup -> AppDatabase.playbookDao.insertPlaybookDeviceGroup(
                                relation = PlaybookDeviceGroupRelationEntity(it.id, uiModel.id)
                            )
                        }
                    }
                    onExit()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
