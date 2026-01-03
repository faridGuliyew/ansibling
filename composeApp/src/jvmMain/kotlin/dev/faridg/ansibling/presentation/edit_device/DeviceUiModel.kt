package dev.faridg.ansibling.presentation.edit_device

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.faridg.ansibling.domain.Device

data class DeviceUiModel(
    val nickName: MutableState<String>,
    val username: MutableState<String>,
    val password: MutableState<String>,
    val port: MutableState<Int>,
    val ip: MutableState<String>
)

fun Device.toUiModel(): DeviceUiModel {
    return DeviceUiModel(
        nickName = mutableStateOf(nickName),
        username = mutableStateOf(username),
        password = mutableStateOf(password),
        port = mutableStateOf(port),
        ip = mutableStateOf(ip),
    )
}
fun DeviceUiModel.toDomain(id: String): Device {
    return Device(
        id = id,
        nickName = nickName.value,
        username = username.value,
        password = password.value,
        port = port.value,
        ip = ip.value
    )
}