package dev.faridg.ansibling.data.room.entity.playbook.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity

@Entity(
    tableName = "playbook_device_relations",
    primaryKeys = ["deviceId", "playbookId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = arrayOf("playbookId"),
            childColumns = arrayOf("playbookId"),
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
data class PlaybookDeviceRelationEntity(
    val deviceId: String,
    val playbookId: String
)