package dev.faridg.ansibling.presentation.home_screen.device_groups_tab

import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.domain.DeviceGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class DeviceGroupsViewModel {

    val deviceGroups: MutableStateFlow<List<DeviceGroup>> = MutableStateFlow(emptyList())

    val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        viewModelScope.launch {
            AppDatabase.deviceDao
                .getAllDeviceGroupsWithDevicesAsFlow()
                .collect { entities ->
                    deviceGroups.update { entities.map { it.toDomain() } }
                }
        }
    }

    fun addGroup() {
        viewModelScope.launch {
            AppDatabase.deviceDao.insertDeviceGroup(
                DeviceGroupEntity(
                    groupId = UUID.randomUUID().toString(),
                    nickName = "New Device Group"
                )
            )
        }
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            AppDatabase.deviceDao.deleteDeviceGroupById(groupId)
        }
    }
}
