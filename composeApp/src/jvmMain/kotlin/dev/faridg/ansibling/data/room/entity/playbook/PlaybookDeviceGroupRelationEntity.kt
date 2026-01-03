package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity

@Entity(
    tableName = "playbook_device_group_relations",
    primaryKeys = ["groupId", "playbookId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = arrayOf("playbookId"),
            childColumns = arrayOf("playbookId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DeviceGroupEntity::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("groupId"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PlaybookDeviceGroupRelationEntity(
    val groupId: String,
    val playbookId: String
)