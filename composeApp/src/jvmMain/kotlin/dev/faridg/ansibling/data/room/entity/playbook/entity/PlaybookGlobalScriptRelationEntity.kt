package dev.faridg.ansibling.data.room.entity.playbook.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity

@Entity(
    tableName = "playbook_global_scripts",
    primaryKeys = ["scriptId", "playbookId", "order"],
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = arrayOf("playbookId"),
            childColumns = arrayOf("playbookId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScriptEntity::class,
            parentColumns = ["scriptId"],
            childColumns = ["scriptId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaybookGlobalScriptRelationEntity(
    val scriptId: String,
    val playbookId: String,
    val order: Int
)