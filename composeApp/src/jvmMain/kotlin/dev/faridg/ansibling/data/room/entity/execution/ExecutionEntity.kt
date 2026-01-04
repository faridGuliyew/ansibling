package dev.faridg.ansibling.data.room.entity.execution

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookEntity
import dev.faridg.ansibling.domain.TaskStatusType

@Entity(
    tableName = "executions",
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = ["playbookId"],
            childColumns = ["playbookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExecutionEntity (
    @PrimaryKey(autoGenerate = true)
    val executionId: Long = 0,
    val nickName: String = "Unnamed execution",
    val playbookId: String,
    val status: TaskStatusType
)