package dev.faridg.ansibling.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookDeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookWithActionsAndDevicesRelation
import dev.faridg.ansibling.data.room.entity.playbook.RemoteActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybookDao {

    @Query("DELETE FROM remote_actions WHERE playbookId = :playbookId")
    suspend fun deleteRemoteActionsForPlaybook(playbookId: String)

    @Query("DELETE FROM playbooks WHERE playbookId = :playbookId")
    suspend fun deletePlaybook(playbookId: String)

    @Transaction
    @Query("SELECT * FROM playbooks")
    fun getPlaybooksWithActions(): Flow<List<PlaybookWithActionsAndDevicesRelation>>

    @Upsert
    suspend fun insertRemoteActions(actions: List<RemoteActionEntity>)

    @Upsert
    suspend fun insertPlaybook(playbook: PlaybookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaybookDevice(relation: PlaybookDeviceRelationEntity)

    @Query("DELETE FROM playbook_device_relations WHERE playbookId = :playbookId")
    suspend fun deletePlaybookDevices(playbookId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaybookDeviceGroup(relation: PlaybookDeviceGroupRelationEntity)

    @Query("DELETE FROM playbook_device_group_relations WHERE playbookId = :playbookId")
    suspend fun deletePlaybookDeviceGroups(playbookId: String)
}