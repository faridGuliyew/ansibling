package dev.faridg.ansibling.presentation.edit_device_group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_add
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.domain.Device
import dev.faridg.ansibling.domain.DeviceGroup
import dev.faridg.ansibling.ui_kit.ExpandableCard
import dev.faridg.ansibling.ui_kit.device.DeviceItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun EditDeviceGroupScreen(
    deviceGroupWithDevices: DeviceGroup,
    onExit: () -> Unit
) {
    val uiModel = remember { deviceGroupWithDevices.toUiModel() }
    val allDevices = remember { mutableStateListOf<Device>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val existingIds = uiModel.devices.map { it.id }.toSet()
        allDevices.addAll(
            AppDatabase.deviceDao.getAllDevicesAsFlow().first()
                .filter { it.deviceId !in existingIds }
                .map { it.toDomain() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiModel.nickName.value,
            onValueChange = { uiModel.nickName.value = it },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )

        ExpandableCard(
            title = {
                Text(text = "Added devices (${uiModel.devices.size})")
            }
        ) {
            LazyColumn (
                modifier = Modifier.heightIn(max = 500.dp)
            ) {
                itemsIndexed(uiModel.devices) { index, it ->
                    DeviceItem(
                        item = it,
                        onIconClick = {
                            allDevices.add(it)
                            uiModel.devices.removeAt(index)
                        }
                    )
                }
            }
        }

        ExpandableCard(
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
                        DeviceItem(
                            item = it,
                            iconRes = Res.drawable.ic_add,
                            onIconClick = {
                                uiModel.devices.add(it)
                                allDevices.removeAt(index)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    // Update nickname
                    AppDatabase.deviceDao.insertDeviceGroup(DeviceGroupEntity(uiModel.groupId, uiModel.nickName.value))
                    // Update relations
                    AppDatabase.deviceDao.deleteAllDeviceGroupRelations(uiModel.groupId)
                    AppDatabase.deviceDao.insertDeviceRelations(
                        uiModel.devices.map { DeviceGroupRelationEntity(
                            deviceId = it.id, groupId = uiModel.groupId
                        ) }
                    )
                    onExit()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}