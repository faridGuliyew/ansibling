package dev.faridg.ansibling.presentation.edit_ci_actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.Playbook
import dev.faridg.ansibling.domain.PlaybookDevice
import dev.faridg.ansibling.domain.PlaybookScript
import dev.faridg.ansibling.domain.Script
import dev.faridg.ansibling.domain.ScriptType
import java.util.UUID

data class PlaybookUiModel(
    val id: String,
    val playbookId: String,
    val nickname: MutableState<String>,
    val scripts: SnapshotStateList<PlaybookScriptUiModel>,
    val devices: SnapshotStateList<PlaybookDevice>
)

fun Playbook.toUiModel() = PlaybookUiModel(
    id = this.id,
    playbookId = this.id,
    nickname = mutableStateOf(nickName),
    scripts = actions.map { it.toUiModel() }.toMutableStateList(),
    devices = devices.toMutableStateList(),
)

data class PlaybookScriptUiModel(
    val id: String,
    val playbookId: String,
    val globalScriptId: String?,
    val title: MutableState<String>,
    val content: MutableState<String>,
    val commandType: MutableState<ScriptType>,
    val exceptionBehavior: MutableState<ExceptionBehavior>,
    val order: Int,
    val expanded: MutableState<Boolean> = mutableStateOf(false)
)

fun PlaybookScript.toUiModel() = PlaybookScriptUiModel(
    id = scriptId,
    playbookId = playbookId,
    globalScriptId = globalScriptId,
    title = mutableStateOf(title),
    content = mutableStateOf(content),
    commandType = mutableStateOf(commandType),
    exceptionBehavior = mutableStateOf(exceptionBehaviour),
    order = order
)

fun PlaybookScriptUiModel.toDomain(): PlaybookScript {
    return PlaybookScript(
        scriptId = id,
        playbookId = playbookId,
        globalScriptId = globalScriptId,
        title = title.value,
        content = content.value,
        commandType = commandType.value,
        exceptionBehaviour = exceptionBehavior.value,
        order = order
    )
}

fun Script.toPlaybookScriptUiModel(
    playbookId: String,
    order: Int
) = PlaybookScriptUiModel(
    id = UUID.randomUUID().toString(),
    playbookId = playbookId,
    globalScriptId = scriptId,
    title = mutableStateOf(title),
    content = mutableStateOf(content),
    commandType = mutableStateOf(type),
    exceptionBehavior = mutableStateOf(exceptionBehavior),
    order = order
)

fun PlaybookScriptUiModel.toScript() = Script(
    scriptId = globalScriptId ?: error("globalScriptId is required!"),
    title = title.value,
    content = content.value,
    type = commandType.value,
    exceptionBehavior = exceptionBehavior.value
)