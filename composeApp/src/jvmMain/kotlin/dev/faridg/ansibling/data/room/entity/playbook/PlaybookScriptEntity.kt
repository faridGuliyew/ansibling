package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.ScriptType

@Entity(
    tableName = "remote_actions",
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = ["playbookId"],
            childColumns = ["playbookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScriptEntity::class,
            parentColumns = ["scriptId"],
            childColumns = ["globalScriptId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaybookScriptEntity(
    @PrimaryKey
    val scriptId: String,
    val playbookId: String,
    val globalScriptId: String?,
    val title: String,
    val command: String,
    val commandType: ScriptType,
    val exceptionBehaviour: ExceptionBehavior,
    val order: Int
)