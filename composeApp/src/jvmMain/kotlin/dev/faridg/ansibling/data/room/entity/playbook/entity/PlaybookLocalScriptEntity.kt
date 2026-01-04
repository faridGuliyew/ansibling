package dev.faridg.ansibling.data.room.entity.playbook.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.ScriptType

@Entity(
    tableName = "playbook_local_scripts",
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = ["playbookId"],
            childColumns = ["playbookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaybookLocalScriptEntity(
    @PrimaryKey
    val scriptId: String,
    val playbookId: String,
    val title: String,
    val content: String,
    val type: ScriptType,
    val exceptionBehaviour: ExceptionBehavior,
    val order: Int
)