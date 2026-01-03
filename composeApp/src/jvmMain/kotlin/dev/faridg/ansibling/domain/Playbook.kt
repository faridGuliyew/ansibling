package dev.faridg.ansibling.domain

data class Playbook(
    val id: String,
    val nickName: String,
    val actions: List<RemoteAction>,
    val devices: List<PlaybookDevice>
)


data class RemoteAction(
    val id: String,
    val playbookId: String,
    val command: String,
    val commandType: RemoteActionCommandType,
    val exceptionBehaviour: ExceptionBehavior,
    val order: Int
)