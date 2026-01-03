package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.RemoteActionCommandType

@Entity(
    tableName = "remote_actions",
    foreignKeys = [
        ForeignKey(
            entity = PlaybookEntity::class,
            parentColumns = ["playbookId"],
            childColumns = ["playbookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RemoteActionEntity(
    @PrimaryKey
    val actionId: String,
    val playbookId: String,
    val command: String,
    val commandType: RemoteActionCommandType,
    val exceptionBehaviour: ExceptionBehavior,
    val order: Int
)