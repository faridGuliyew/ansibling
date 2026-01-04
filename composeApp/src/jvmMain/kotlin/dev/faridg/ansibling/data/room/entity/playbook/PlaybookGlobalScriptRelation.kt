package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Embedded
import androidx.room.Relation
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookGlobalScriptRelationEntity
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity

data class PlaybookGlobalScriptRelation(
    @Embedded
    val relation: PlaybookGlobalScriptRelationEntity,

    @Relation(
        parentColumn = "scriptId",
        entityColumn = "scriptId"
    )
    val script: ScriptEntity
)