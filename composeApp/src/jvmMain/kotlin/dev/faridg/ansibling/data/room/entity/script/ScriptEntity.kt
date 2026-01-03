package dev.faridg.ansibling.data.room.entity.script

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.ScriptType

@Entity(tableName = "scripts")
data class ScriptEntity(
    @PrimaryKey
    val scriptId: String,
    val title: String,
    val content: String,
    val type: ScriptType,
    val exceptionBehaviour: ExceptionBehavior
)