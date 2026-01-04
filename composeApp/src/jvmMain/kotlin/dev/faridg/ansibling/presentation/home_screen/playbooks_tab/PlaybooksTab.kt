package dev.faridg.ansibling.presentation.home_screen.playbooks_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.faridg.ansibling.WindowRoute
import dev.faridg.ansibling.ui_kit.playbook.PlaybookItem
import dev.faridg.ansibling.ui_kit.tab.TabScreen
import dev.faridg.ansibling.windowManager

@Composable
fun PlaybooksTab() {
    val vm = remember { PlaybooksViewModel() }
    val playbooks by vm.playbooks.collectAsStateWithLifecycle()

    TabScreen(
        title = "Playbooks",
        onAdd = vm::addPlaybook
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playbooks) { playbook ->
                PlaybookItem(
                    playbook = playbook,
                    onEdit = {
                        vm.editPlaybook(playbook.id)
                    },
                    onExecute = {
                        vm.executePlaybook(playbook.id)
                    },
                    onDelete = {
                        vm.deletePlaybook(playbook.id)
                    }
                )
            }
        }
    }
}