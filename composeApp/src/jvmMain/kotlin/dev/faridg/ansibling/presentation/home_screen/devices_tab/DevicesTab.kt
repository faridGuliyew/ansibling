package dev.faridg.ansibling.presentation.home_screen.devices_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.faridg.ansibling.WindowRoute
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.ui_kit.device.DeviceItem
import dev.faridg.ansibling.ui_kit.tab.TabScreen
import dev.faridg.ansibling.windowManager
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun DevicesTab(
) {
    val vm = remember { DevicesViewModel(AppDatabase.deviceDao) }
    val devices by vm.devices.collectAsStateWithLifecycle()

    TabScreen(
        title = "Devices",
        onAdd = vm::addDevice
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxHeight(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = devices, key = { it.id }) {
                DeviceItem(
                    item = it,
                    onEdit = { windowManager.push(WindowRoute.EditDevice(it)) },
                    onDelete = { vm.deleteDevice(it.id) }
                )
            }
        }
    }
}