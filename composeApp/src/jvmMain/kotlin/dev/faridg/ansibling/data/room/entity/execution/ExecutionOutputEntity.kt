package dev.faridg.ansibling.data.room.entity.execution

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.faridg.ansibling.domain.StatusType

@Entity(
    tableName = "execution_outputs",
    foreignKeys = [
        ForeignKey(
            entity = ExecutionEntity::class,
            parentColumns = ["executionId"],
            childColumns = ["executionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExecutionOutputEntity (
    @PrimaryKey(autoGenerate = true)
    val outputId: Long = 0,
    val executionId: Long,
    val output: String,
    val type: StatusType
)