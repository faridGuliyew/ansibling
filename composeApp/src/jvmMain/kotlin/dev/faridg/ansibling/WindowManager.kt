package dev.faridg.ansibling

import androidx.compose.runtime.mutableStateListOf
import dev.faridg.ansibling.domain.Device
import dev.faridg.ansibling.domain.DeviceGroup
import dev.faridg.ansibling.domain.Playbook
import dev.faridg.ansibling.domain.Script

sealed interface WindowRoute {
    val title: String
    data class EditDevice(val device: Device, override val title: String = device.nickName) : WindowRoute
    data class EditDeviceGroup(val group: DeviceGroup, override val title: String = group.nickName) : WindowRoute
    data class EditPlaybook(val playbook: Playbook, override val title: String = playbook.nickName) : WindowRoute
    data class ExecutePlaybook(val playbook: Playbook, override val title: String = playbook.nickName) : WindowRoute
    data class ScriptEditor(val script: Script?, override val title: String = script?.title.orEmpty()) : WindowRoute
}

class WindowManager {
    private val activeAppWindows = mutableStateListOf<WindowRoute>()
    val windows : List<WindowRoute> = activeAppWindows

    fun pop(route: WindowRoute) {
        activeAppWindows.remove(route)
    }
    fun push(route: WindowRoute) {
        activeAppWindows.add(route)
    }
}

val windowManager = WindowManager()