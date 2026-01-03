package dev.faridg.ansibling.presentation.edit_ci_actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.Playbook
import dev.faridg.ansibling.domain.PlaybookDevice
import dev.faridg.ansibling.domain.RemoteAction
import dev.faridg.ansibling.domain.RemoteActionCommandType

data class PlaybookUiModel(
    val id: String,
    val playbookId: String,
    val nickname: MutableState<String>,
    val remoteActions: SnapshotStateList<RemoteActionUiModel>,
    val devices: SnapshotStateList<PlaybookDevice>
)

fun Playbook.toUiModel() = PlaybookUiModel(
    id = this.id,
    playbookId = this.id,
    nickname = mutableStateOf(nickName),
    remoteActions = actions.map { it.toUiModel() }.toMutableStateList(),
    devices = devices.toMutableStateList(),
)

data class RemoteActionUiModel(
    val id: String,
    val playbookId: String,
    val command: MutableState<String>,
    val commandType: MutableState<RemoteActionCommandType>,
    val exceptionBehavior: MutableState<ExceptionBehavior>,
    val order: Int,
    val expanded: MutableState<Boolean> = mutableStateOf(false)
)

fun RemoteAction.toUiModel() = RemoteActionUiModel(
    id = id,
    playbookId = playbookId,
    command = mutableStateOf(command),
    commandType = mutableStateOf(commandType),
    exceptionBehavior = mutableStateOf(exceptionBehaviour),
    order = order
)

fun RemoteActionUiModel.toDomain(): RemoteAction {
    return RemoteAction(
        id = id,
        playbookId = playbookId,
        command = command.value,
        commandType = commandType.value,
        exceptionBehaviour = exceptionBehavior.value,
        order = order
    )
}