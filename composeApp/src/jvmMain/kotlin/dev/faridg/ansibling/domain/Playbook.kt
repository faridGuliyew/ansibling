package dev.faridg.ansibling.domain

data class Playbook(
    val id: String,
    val nickName: String,
    val actions: List<PlaybookScript>,
    val devices: List<PlaybookDevice>
)


data class PlaybookScript(
    val scriptId: String,
    val playbookId: String,
    val globalScriptId: String?,
    val title: String,
    val content: String,
    val commandType: ScriptType,
    val exceptionBehaviour: ExceptionBehavior,
    val order: Int
)