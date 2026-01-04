package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Embedded
import androidx.room.Relation
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookGlobalScriptRelationEntity
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity

data class PlaybookDeviceRelation(
    @Embedded
    val relation: PlaybookDeviceRelationEntity,

    @Relation(
        parentColumn = "scriptId",
        entityColumn = "scriptId"
    )
    val device: DeviceEntity
)