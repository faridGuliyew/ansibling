package dev.faridg.ansibling.domain


sealed interface PlaybookDevice

data class Device(
    val id: String,
    val nickName: String,
    val username: String,
    val password: String,
    val port: Int,
    val ip: String
) : PlaybookDevice

data class DeviceGroup(
    val id: String,
    val nickName: String,
    val devices: List<Device>
) : PlaybookDevice