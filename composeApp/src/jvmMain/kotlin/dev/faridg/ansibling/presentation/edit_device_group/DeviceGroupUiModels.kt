package dev.faridg.ansibling.presentation.edit_device_group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import dev.faridg.ansibling.domain.Device
import dev.faridg.ansibling.domain.DeviceGroup

data class DeviceGroupWithDevicesUiModel(
    val groupId: String,
    val nickName: MutableState<String>,
    val devices: SnapshotStateList<Device>
)

fun DeviceGroup.toUiModel(): DeviceGroupWithDevicesUiModel {
    return DeviceGroupWithDevicesUiModel(
        groupId = id,
        nickName = mutableStateOf(nickName),
        devices = devices.toMutableStateList()
    )
}
fun DeviceGroupWithDevicesUiModel.toDomain(): DeviceGroup {
    return DeviceGroup(
        id = groupId,
        nickName = nickName.value,
        devices = devices
    )
}