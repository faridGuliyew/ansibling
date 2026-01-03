package dev.faridg.ansibling.data.room.entity.device_group

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity

data class DeviceGroupWithDevicesRelation(
    @Embedded
    val deviceGroup: DeviceGroupEntity,

    @Relation(
        parentColumn = "groupId",
        entityColumn = "deviceId",
        associateBy = Junction(
            value = DeviceGroupRelationEntity::class,
            parentColumn = "groupId",
            entityColumn = "deviceId"
        )
    )
    val devices: List<DeviceEntity>
)