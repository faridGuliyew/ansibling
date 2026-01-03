package dev.faridg.ansibling.presentation.home_screen.device_groups_tab

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
import dev.faridg.ansibling.ui_kit.device_group.DeviceGroupItem
import dev.faridg.ansibling.ui_kit.tab.TabScreen
import dev.faridg.ansibling.windowManager
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun DeviceGroupsTab(
) {
    val vm = remember { DeviceGroupsViewModel() }
    val deviceGroups by vm.deviceGroups.collectAsStateWithLifecycle()

    TabScreen(
        title = "Device groups",
        onAdd = vm::addGroup
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxHeight(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(deviceGroups) { group ->
                DeviceGroupItem(
                    group = group,
                    onEdit = {
                        windowManager.push(WindowRoute.EditDeviceGroup(group))
                    },
                    onDelete = {
                        vm.deleteGroup(group.id)
                    }
                )
            }
        }
    }
}