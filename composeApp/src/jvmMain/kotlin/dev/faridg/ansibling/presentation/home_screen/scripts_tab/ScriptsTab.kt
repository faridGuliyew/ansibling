package dev.faridg.ansibling.presentation.home_screen.scripts_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.faridg.ansibling.WindowRoute
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.domain.Script
import dev.faridg.ansibling.ui_kit.script.ScriptItem
import dev.faridg.ansibling.ui_kit.tab.TabScreen
import dev.faridg.ansibling.windowManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ScriptsTab() {
    val scripts = remember {
        mutableStateListOf<Script>()
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        AppDatabase.scriptDao.observeAll().collectLatest {
            scripts.clear()
            scripts.addAll(it.map { it.toDomain() })
        }
    }

    TabScreen(
        title = "Scripts",
        onAdd = {
            windowManager.push(WindowRoute.ScriptEditor(null))
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(items = scripts) { index, script ->
                ScriptItem(
                    item = script,
                    onEdit = {
                        windowManager.push(WindowRoute.ScriptEditor(script))
                    },
                    onDelete = {
                        scope.launch {
                            AppDatabase.scriptDao.delete(script.id)
                        }
                    })
            }
        }
    }
}