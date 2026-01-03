package dev.faridg.ansibling.presentation.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.faridg.ansibling.presentation.home_screen.device_groups_tab.DeviceGroupsTab
import dev.faridg.ansibling.presentation.home_screen.devices_tab.DevicesTab
import dev.faridg.ansibling.presentation.home_screen.playbooks_tab.PlaybooksTab
import dev.faridg.ansibling.presentation.home_screen.scripts_tab.ScriptsTab
import dev.faridg.ansibling.presentation.home_screen.variables_tab.VariablesTab

enum class HomeTab {
    DEVICES,
    DEVICE_GROUPS,
    PLAYBOOKS,
    VARIABLES,
    SCRIPTS
}

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(HomeTab.DEVICES) }

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(selectedTabIndex = selectedTab.ordinal) {
            Tab(
                selected = selectedTab == HomeTab.DEVICES,
                onClick = { selectedTab = HomeTab.DEVICES },
                text = { Text("Devices") }
            )
            Tab(
                selected = selectedTab == HomeTab.DEVICE_GROUPS,
                onClick = { selectedTab = HomeTab.DEVICE_GROUPS },
                text = { Text("Device Groups") }
            )
            Tab(
                selected = selectedTab == HomeTab.PLAYBOOKS,
                onClick = { selectedTab = HomeTab.PLAYBOOKS },
                text = { Text("Playbooks") }
            )
            Tab(
                selected = selectedTab == HomeTab.VARIABLES,
                onClick = { selectedTab = HomeTab.VARIABLES },
                text = { Text("Variables") }
            )
            Tab(
                selected = selectedTab == HomeTab.SCRIPTS,
                onClick = { selectedTab = HomeTab.SCRIPTS },
                text = { Text("Scripts") }
            )
        }

        Spacer(Modifier.height(12.dp))

        when (selectedTab) {
            HomeTab.DEVICES -> DevicesTab()
            HomeTab.DEVICE_GROUPS -> DeviceGroupsTab()
            HomeTab.VARIABLES -> VariablesTab()
            HomeTab.SCRIPTS -> ScriptsTab()
            HomeTab.PLAYBOOKS -> PlaybooksTab()
        }
    }
}