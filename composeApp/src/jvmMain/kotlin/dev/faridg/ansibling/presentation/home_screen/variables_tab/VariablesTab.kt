package dev.faridg.ansibling.presentation.home_screen.variables_tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_trash
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.data.room.toEntity
import dev.faridg.ansibling.ui_kit.tab.TabScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.util.*

@Composable
fun VariablesTab() {
    val savedVariables = remember {
        mutableStateListOf<VariableUiModel>()
    }
    val unsavedVariables = remember {
        mutableStateListOf<VariableUiModel>()
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        AppDatabase.variableDao.observeAll().collectLatest {
            savedVariables.clear()
            savedVariables.addAll(it.map { it.toDomain().toUiModel() })
        }
    }

    TabScreen(
        title = "Variables",
        onAdd = {
            unsavedVariables.add(
                VariableUiModel(
                    id = UUID.randomUUID().toString(),
                    key = mutableStateOf(""),
                    value = mutableStateOf(""),
                    hasUnsavedChanges = mutableStateOf(false)
                )
            )
        }
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(items = savedVariables + unsavedVariables) { index, variable ->
                VariableItem(
                    variable = variable,
                    onSave = {
                        scope.launch {
                            val unsavedIndex = unsavedVariables.indexOfFirst { variable.key == it.key }
                            when (unsavedIndex) {
                                -1 -> {
                                    AppDatabase.variableDao.insert(variable.toDomain().toEntity())
                                    variable.hasUnsavedChanges.value = false
                                }
                                else -> {
                                    unsavedVariables.removeAt(unsavedIndex)
                                    AppDatabase.variableDao.insert(variable.toDomain().toEntity())
                                }
                            }
                        }
                    },
                    onDelete = {
                        scope.launch {
                            AppDatabase.variableDao.delete(variable.id)
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun VariableItem(
    variable: VariableUiModel,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = variable.key.value,
            onValueChange = {
                variable.key.value = it
                variable.hasUnsavedChanges.value = true
            },
            label = { Text("Key") },
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = variable.value.value,
            onValueChange = {
                variable.value.value = it
                variable.hasUnsavedChanges.value = true
            },
            label = { Text("Value") },
            modifier = Modifier.weight(1f)
        )
        if (variable.key.value.isBlank()) return@Row

        when (variable.hasUnsavedChanges.value) {
            true -> {
                TextButton(onClick = onSave) {
                    Text("Save")
                }
            }
            false -> {
                IconButton(
                    onClick = onDelete
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_trash),
                        contentDescription = null
                    )
                }
            }
        }
    }
}