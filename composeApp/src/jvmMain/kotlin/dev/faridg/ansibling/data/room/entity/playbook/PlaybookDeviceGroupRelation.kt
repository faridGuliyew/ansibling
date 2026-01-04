package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Embedded
import androidx.room.Relation
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookGlobalScriptRelationEntity
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity

data class PlaybookDeviceGroupRelation(
    @Embedded
    val relation: PlaybookDeviceGroupRelationEntity,

    @Relation(
        parentColumn = "scriptId",
        entityColumn = "scriptId"
    )
    val deviceGroup: DeviceGroupEntity
)