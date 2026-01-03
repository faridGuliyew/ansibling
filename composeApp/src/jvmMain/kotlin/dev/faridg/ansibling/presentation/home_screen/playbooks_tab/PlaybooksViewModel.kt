package dev.faridg.ansibling.presentation.home_screen.playbooks_tab


import androidx.lifecycle.ViewModel
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookEntity
import dev.faridg.ansibling.data.room.toDomain
import dev.faridg.ansibling.domain.Playbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class PlaybooksViewModel : ViewModel() {

    private val _playbooks = MutableStateFlow<List<Playbook>>(emptyList())
    val playbooks: StateFlow<List<Playbook>> = _playbooks

    val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        viewModelScope.launch {
            AppDatabase.playbookDao
                .getPlaybooksWithActions()
                .collectLatest { entities ->
                    _playbooks.value = entities.map { it.toDomain() }
                }
        }
    }

    fun addPlaybook() {
        viewModelScope.launch {
            AppDatabase.playbookDao.insertPlaybook(
                PlaybookEntity(
                    playbookId = UUID.randomUUID().toString(),
                    nickName = "New playbook"
                )
            )
        }
    }

    fun deletePlaybook(playbookId: String) {
        viewModelScope.launch {
            AppDatabase.playbookDao.deletePlaybook(playbookId)
        }
    }
}
