package dev.faridg.ansibling

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.faridg.ansibling.presentation.edit_ci_actions.EditPlaybookScreen
import dev.faridg.ansibling.presentation.edit_device.EditDeviceScreen
import dev.faridg.ansibling.presentation.edit_device_group.EditDeviceGroupScreen
import dev.faridg.ansibling.presentation.execute_ci.ExecutePlaybookScreen
import dev.faridg.ansibling.presentation.home_screen.HomeScreen
import dev.faridg.ansibling.presentation.home_screen.scripts_tab.ScriptEditorScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ansibling",
    ) {
        HomeScreen()
    }

    val scope = rememberCoroutineScope()

    windowManager.windows.forEach { windowRoute ->
        key(windowRoute::class.simpleName) {
            Window(
                onCloseRequest = { windowManager.pop(windowRoute) },
                title = windowRoute.title,
            ) {
//                val focusManager = LocalFocusManager.current
                Box (
                    modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
//                        focusManager.clearFocus()
                    }
                ) {
                    when (windowRoute) {
                        is WindowRoute.EditPlaybook -> {
                            EditPlaybookScreen(
                                playbook = windowRoute.playbook,
                                onExit = {
                                    windowManager.pop(windowRoute)
                                },
//                                onSave = {
//                                    scope.launch {
//                                        AppDatabase.deviceDao.updateActions(
//                                            item = windowRoute.device,
//                                            actions = it
//                                        )
//                                    }
//                                    windowManager.pop(windowRoute)
//                                }
                            )
                        }
                        is WindowRoute.EditDevice -> {
                            EditDeviceScreen(
                                device = windowRoute.device,
                                onExit = {
                                    windowManager.pop(windowRoute)
                                }
                            )
                        }

                        is WindowRoute.ExecutePlaybook -> {
                            ExecutePlaybookScreen(playbook = windowRoute.playbook)
                        }

                        is WindowRoute.ScriptEditor -> {
                            ScriptEditorScreen(windowRoute.script, onExit = {
                                windowManager.pop(windowRoute)
                            })
                        }

                        is WindowRoute.EditDeviceGroup -> {
                            EditDeviceGroupScreen(
                                deviceGroupWithDevices = windowRoute.group,
                                onExit = {
                                    windowManager.pop(windowRoute)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}