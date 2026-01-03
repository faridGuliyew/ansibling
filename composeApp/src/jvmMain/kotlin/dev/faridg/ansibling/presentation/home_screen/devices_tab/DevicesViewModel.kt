package dev.faridg.ansibling.presentation.home_screen.devices_tab

import dev.faridg.ansibling.data.room.dao.DeviceDao
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.data.room.toEntity
import dev.faridg.ansibling.domain.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class DevicesViewModel(
    private val deviceDao: DeviceDao
) {

    val devices: MutableStateFlow<List<Device>> = MutableStateFlow(emptyList())

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        viewModelScope.launch {
            deviceDao.getAllDevicesAsFlow().collect { entities ->
                devices.update {  entities.map { it.toDomain() } }
            }
        }
    }

    fun addDevice() {
        viewModelScope.launch {
            deviceDao.insertDevice(
                Device(
                    id = UUID.randomUUID().toString(),
                    nickName = "Nickname",
                    username = "username",
                    password = "",
                    port = 22,
                    ip = "127.0.0.1"
                ).toEntity()
            )
        }
    }

    fun deleteDevice(id: String) {
        viewModelScope.launch {
            try {
                deviceDao.deleteDeviceById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }
}
