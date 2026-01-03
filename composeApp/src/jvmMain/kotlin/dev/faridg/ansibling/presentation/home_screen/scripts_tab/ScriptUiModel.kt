package dev.faridg.ansibling.presentation.home_screen.scripts_tab

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.faridg.ansibling.domain.ExceptionBehavior
import dev.faridg.ansibling.domain.Script
import dev.faridg.ansibling.domain.ScriptType

class ScriptUiModel(
    val id: String,
    val name: MutableState<String>,
    val content: MutableState<String>,
    val type: MutableState<ScriptType>,
    val exceptionBehaviour: MutableState<ExceptionBehavior>
)

fun ScriptUiModel.toDomain() : Script = Script(
    scriptId = id,
    title = name.value,
    content = content.value,
    type = type.value,
    exceptionBehavior = exceptionBehaviour.value
)

fun Script.toUiModel() : ScriptUiModel = ScriptUiModel(
    id = scriptId,
    name = mutableStateOf(title),
    content = mutableStateOf(content),
    type = mutableStateOf(type),
    exceptionBehaviour = mutableStateOf(exceptionBehavior)
)