package dev.faridg.ansibling.presentation.home_screen.scripts_tab

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.faridg.ansibling.domain.Script

class ScriptUiModel(
    val id: String,
    val name: MutableState<String>,
    val content: MutableState<String>
)

fun ScriptUiModel.toDomain() : Script = Script(
    id = id,
    name = name.value,
    content = content.value
)

fun Script.toUiModel() : ScriptUiModel = ScriptUiModel(
    id = id,
    name = mutableStateOf(name),
    content = mutableStateOf(content)
)