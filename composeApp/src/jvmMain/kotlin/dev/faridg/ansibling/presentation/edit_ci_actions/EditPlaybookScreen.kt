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
import ansibling.composeapp.generated.resources.ic_script
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.util.*


@Composable
fun EditPlaybookScreen(
    playbook: Playbook,
    onExit: () -> Unit
) {
    val playbookUiModel = remember { playbook.toUiModel() }
    val allDevices = remember { mutableStateListOf<PlaybookDevice>() }
    val allScripts = remember { mutableStateListOf<Script>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val existingIds = playbookUiModel.devices.map {
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
        val globalScripts = AppDatabase.scriptDao.observeAll().first()
        val indexedGlobalScripts = globalScripts.associateBy { it.scriptId }
        allScripts.clear()
        allScripts.addAll(globalScripts.map { it.toDomain() })

        // Check if any script needs to be updated.
        val includesGlobalScript = playbookUiModel.scripts.any { it.globalScriptId != null }
        if (!includesGlobalScript) return@LaunchedEffect

        playbookUiModel.scripts.forEach {
            if (it.globalScriptId == null) return@forEach

            it.apply {
                // TODO - add any updates that need to be reflected here.
                title.value = indexedGlobalScripts[globalScriptId]!!.title
            }
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
            value = playbookUiModel.nickname.value,
            onValueChange = { playbookUiModel.nickname.value = it },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )

        // == ADD DEVICE SECTION START ==
        ExpandableContent(
            title = {
                Text(text = "Added devices (${playbookUiModel.devices.size})")
            }
        ) {
            LazyColumn (
                modifier = Modifier.heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(playbookUiModel.devices) { index, it ->
                    when (it) {
                        is Device -> DeviceItem(
                            item = it,
                            onIconClick = {
                                allDevices.add(it)
                                playbookUiModel.devices.removeAt(index)
                            }
                        )
                        is DeviceGroup -> DeviceGroupItem(
                            group = it,
                            onIconClick = {
                                allDevices.add(it)
                                playbookUiModel.devices.removeAt(index)
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
                                    playbookUiModel.devices.add(it)
                                    allDevices.removeAt(index)
                                },
                                iconRes = Res.drawable.ic_add
                            )
                            is DeviceGroup -> DeviceGroupItem(
                                group = it,
                                onIconClick = {
                                    playbookUiModel.devices.add(it)
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
        ExpandableContent(
            title = {
                Text(text = "Added scripts (${playbookUiModel.scripts.size})")
            }
        ) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 500.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    playbookUiModel.scripts
                ) { index, scriptUiModel ->
                    PlaybookScriptItem(
                        scriptUiModel = scriptUiModel,
                        playbookUiModel = playbookUiModel,
                        index = index
                    )
                }
            }
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
                        onClick = {
                            playbookUiModel.scripts.add(
                                it.toPlaybookScriptUiModel(
                                    playbookId = playbookUiModel.playbookId,
                                    order = (playbookUiModel.scripts.maxOfOrNull { it.order } ?: 0) + 1
                                )
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                playbookUiModel.scripts.add(
                    PlaybookScriptUiModel(
                        id = UUID.randomUUID().toString(),
                        playbookId = playbook.id,
                        globalScriptId = null,
                        content = mutableStateOf(""),
                        title = mutableStateOf("Do something"),
                        commandType = mutableStateOf(ScriptType.PLAIN),
                        exceptionBehavior = mutableStateOf(ExceptionBehavior.IGNORE),
                        order = (playbookUiModel.scripts.maxOfOrNull { it.order } ?: 0) + 1
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

        Button(
            onClick = {
                scope.launch {
                    AppDatabase.playbookDao.deleteRemoteActionsForPlaybook(playbook.id)
                    AppDatabase.playbookDao.deletePlaybookDevices(playbook.id)
                    AppDatabase.playbookDao.deletePlaybookDeviceGroups(playbook.id)

                    AppDatabase.playbookDao.insertPlaybook(PlaybookEntity(
                        playbookId = playbookUiModel.id,
                        nickName = playbookUiModel.nickname.value
                    ))
                    AppDatabase.playbookDao.insertRemoteActions(
                        actions = playbookUiModel.scripts.map { it.toDomain().toEntity() }
                    )
                    playbookUiModel.devices.forEach {
                        when (it) {
                            is Device -> AppDatabase.playbookDao.insertPlaybookDevice(
                                relation = PlaybookDeviceRelationEntity(it.id, playbookUiModel.id)
                            )
                            is DeviceGroup -> AppDatabase.playbookDao.insertPlaybookDeviceGroup(
                                relation = PlaybookDeviceGroupRelationEntity(it.id, playbookUiModel.id)
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

@Composable
fun PlaybookScriptItem(
    scriptUiModel: PlaybookScriptUiModel,
    playbookUiModel: PlaybookUiModel,
    index: Int
) {
    val onMoveUp: () -> Unit = {
        runCatching {
            playbookUiModel.scripts.apply {
                val prev = this[index - 1]
                this[index - 1] = this[index]
                this[index] = prev
            }
        }
    }
    val onMoveDown: () -> Unit = {
        runCatching {
            playbookUiModel.scripts.apply {
                val next = this[index + 1]
                this[index + 1] = this[index]
                this[index] = next
            }
        }
    }
    when (scriptUiModel.globalScriptId) {
        null -> PlaybookLocalScriptItem(
            scriptUiModel = scriptUiModel,
            onRemove = { playbookUiModel.scripts.removeAt(index) },
            onMoveUp = onMoveUp,
            onMoveDown = onMoveDown
        )
        else -> PlaybookGlobalScriptItem(
            scriptUiModel = scriptUiModel,
            onRemove = { playbookUiModel.scripts.removeAt(index) },
            onMoveUp = onMoveUp,
            onMoveDown = onMoveDown
        )
    }
}

@Composable
fun PlaybookLocalScriptItem(
    scriptUiModel: PlaybookScriptUiModel,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(10.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // ---- header / title + reorder ----
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = scriptUiModel.title.value,
                onValueChange = { scriptUiModel.title.value = it },
                placeholder = { Text("Step title (optional)") },
                singleLine = true
            )

            Row {
                IconButton(
                    enabled = true, // index > 0,
                    onClick = onMoveUp
                ) { Text("↑") }

                IconButton(
                    enabled = true, // index < playbookUiModel.scripts.lastIndex,
                    onClick = onMoveDown
                ) { Text("↓") }
            }
        }

        // ---- script content ----
        OutlinedTextField(
            value = scriptUiModel.content.value,
            onValueChange = { scriptUiModel.content.value = it },
            label = { Text("Bash / Script") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        // ---- compact actions bar ----
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Left side: behavior toggles
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Exception behavior
                var dropdownExpanded by remember { mutableStateOf(false) }

                TextButton(onClick = { dropdownExpanded = true }) {
                    Text("Exception: ${scriptUiModel.exceptionBehavior.value}")
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    ExceptionBehavior.entries.forEach { behavior ->
                        DropdownMenuItem(
                            text = { Text(behavior.name) },
                            onClick = {
                                scriptUiModel.exceptionBehavior.value = behavior
                                dropdownExpanded = false
                            }
                        )
                    }
                }

                // Jinja toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = scriptUiModel.commandType.value == ScriptType.JINJA,
                        onCheckedChange =  {
                            scriptUiModel.commandType.value =
                                if (it) ScriptType.JINJA else ScriptType.PLAIN
                        }
                    )
                    Text("Jinja")
                }
            }

            // Right side: delete
            IconButton(
                onClick = onRemove
            ) {
                Text("🗑")
            }
        }
    }
}

@Composable
fun PlaybookGlobalScriptItem(
    scriptUiModel: PlaybookScriptUiModel,
    onRemove: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---- Script identity ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(Res.drawable.ic_script),
                    contentDescription = null
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scriptUiModel.title.value,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "bash script",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(
                        onClick = onRemove
                    ) {
                        Text("🗑")
                    }
                    IconButton(
                        enabled = true, // index > 0,
                        onClick = onMoveUp
                    ) { Text("↑") }

                    IconButton(
                        enabled = true, // index < playbookUiModel.scripts.lastIndex,
                        onClick = onMoveDown
                    ) { Text("↓") }
                }
            }
        }
    }
}
