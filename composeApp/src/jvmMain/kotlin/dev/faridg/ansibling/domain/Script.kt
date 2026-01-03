package dev.faridg.ansibling.domain

data class Script(
    val scriptId: String,
    val title: String,
    val content: String,
    val type: ScriptType,
    val exceptionBehavior: ExceptionBehavior
)