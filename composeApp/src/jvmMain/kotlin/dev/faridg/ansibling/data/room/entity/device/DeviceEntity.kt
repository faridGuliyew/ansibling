package dev.faridg.ansibling.data.room.entity.device

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey
    val deviceId: String,
    val nickName: String,
    val username: String,
    val password: String,
    val port: Int,
    val ip: String
)