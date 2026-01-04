package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookGlobalScriptRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookLocalScriptEntity
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity

data class FullPlaybookRelation(
//    @Embedded
    val playbook: PlaybookEntity,
//    @Relation(
//        parentColumn = "playbookId",
//        entityColumn = "playbookId"
//    )
    val localScripts: List<PlaybookLocalScriptEntity>,
//    @Relation(
//        parentColumn = "playbookId",
//        entityColumn = "scriptId",
//        associateBy = Junction(
//            value = PlaybookGlobalScriptRelationEntity::class,
//            parentColumn = "playbookId",
//            entityColumn = "scriptId"
//        )
//    )
    val globalScripts: List<PlaybookGlobalScriptRelation>,
//    @Relation(
//        parentColumn = "playbookId",
//        entityColumn = "deviceId",
//        associateBy = Junction(
//            value = PlaybookDeviceRelationEntity::class,
//            parentColumn = "playbookId",
//            entityColumn = "deviceId"
//        )
//    )
    val devices: List<DeviceEntity>,
//    @Relation(
//        parentColumn = "playbookId",
//        entityColumn = "groupId",
//        associateBy = Junction(
//            value = PlaybookDeviceGroupRelationEntity::class,
//            parentColumn = "playbookId",
//            entityColumn = "groupId"
//        )
//    )
    val deviceGroups: List<DeviceGroupEntity>,
)