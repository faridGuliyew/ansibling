package dev.faridg.ansibling.data.room.entity.script

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scripts")
data class ScriptEntity(
    @PrimaryKey
    val scriptId: String,
    val name: String,
    val content: String
)