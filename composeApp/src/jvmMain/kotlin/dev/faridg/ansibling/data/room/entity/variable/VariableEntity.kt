package dev.faridg.ansibling.data.room.entity.variable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "variables")
data class VariableEntity(
    @PrimaryKey
    val variableId: String,
    val key: String,
    val value: String
)