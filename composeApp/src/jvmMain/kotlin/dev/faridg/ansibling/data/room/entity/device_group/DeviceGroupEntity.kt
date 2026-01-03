package dev.faridg.ansibling.data.room.entity.device_group

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_groups")
data class DeviceGroupEntity(
    @PrimaryKey
    val groupId: String,
    val nickName: String
)