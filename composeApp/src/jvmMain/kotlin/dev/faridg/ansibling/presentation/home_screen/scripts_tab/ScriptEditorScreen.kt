package dev.faridg.ansibling.presentation.home_screen.scripts_tab

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.toEntity
import dev.faridg.ansibling.domain.Script
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ScriptEditorScreen(
    script: Script?,
    onExit: () -> Unit,
) {
    val scriptUiModel = remember {
        (script ?: Script(id = UUID.randomUUID().toString(),"", "")).toUiModel()
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Bash script editor",
            style = MaterialTheme.typography.titleMedium
        )

        Card {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = scriptUiModel.name.value,
                    onValueChange = {
                        scriptUiModel.name.value = it
                    },
                    label = { Text("Script name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = scriptUiModel.content.value,
                    onValueChange = {
                        scriptUiModel.content.value = it
                    },
                    label = { Text("Bash script") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = {
                            val selectedContent = openFileDialog(
                                window = null,
                                title = "Pick a script",
                                allowedExtensions = listOf(".txt", ".sh"),
                                allowMultiSelection = false
                            ).firstOrNull()?.readText().orEmpty()
                            if (selectedContent.isBlank()) return@TextButton

                            scriptUiModel.content.value = selectedContent
                        }
                    ) {
                        Text("Load from file")
                    }

                    Spacer(Modifier.weight(1f))

                    TextButton(
                        onClick = {
                            scope.launch {
                                AppDatabase.scriptDao.insert(scriptUiModel.toDomain().toEntity())
                                onExit()
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}