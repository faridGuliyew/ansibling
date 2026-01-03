package dev.faridg.ansibling.presentation.home_screen.variables_tab

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.faridg.ansibling.domain.Variable

class VariableUiModel(
    val id: String,
    val key: MutableState<String>,
    val value: MutableState<String>,
    val hasUnsavedChanges: MutableState<Boolean>
)

fun Variable.toUiModel() = VariableUiModel(
    id = id,
    key = mutableStateOf(key),
    value = mutableStateOf(value),
    hasUnsavedChanges = mutableStateOf(false)
)

fun VariableUiModel.toDomain() = Variable(
    id = id,
    key = key.value,
    value = value.value
)