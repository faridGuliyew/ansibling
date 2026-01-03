package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity

data class PlaybookWithActionsAndDevicesRelation(
    @Embedded
    val playbook: PlaybookEntity,
    @Relation(
        parentColumn = "playbookId",
        entityColumn = "playbookId"
    )
    val actions: List<RemoteActionEntity>,
    @Relation(
        parentColumn = "playbookId",
        entityColumn = "deviceId",
        associateBy = Junction(
            value = PlaybookDeviceRelationEntity::class,
            parentColumn = "playbookId",
            entityColumn = "deviceId"
        )
    )
    val devices: List<DeviceEntity>,
    @Relation(
        parentColumn = "playbookId",
        entityColumn = "groupId",
        associateBy = Junction(
            value = PlaybookDeviceGroupRelationEntity::class,
            parentColumn = "playbookId",
            entityColumn = "groupId"
        )
    )
    val deviceGroups: List<DeviceGroupEntity>,
)