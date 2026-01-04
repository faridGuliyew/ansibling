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
    val isGlobal: Boolean,
    val title: String,
    val content: String,
    val type: ScriptType,
    val exceptionBehaviour: ExceptionBehavior,
    val order: Int
)