package dev.faridg.ansibling.data.room.entity.device_group

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity

@Entity(
    tableName = "device_group_relations",
    primaryKeys = ["deviceId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = DeviceGroupEntity::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("groupId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DeviceEntity::class,
            parentColumns = arrayOf("deviceId"),
            childColumns = arrayOf("deviceId"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class DeviceGroupRelationEntity(
    val deviceId: String,
    val groupId: String
)