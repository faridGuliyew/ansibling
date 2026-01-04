package dev.faridg.ansibling.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookGlobalScriptRelation
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookEntity
import dev.faridg.ansibling.data.room.entity.playbook.FullPlaybookRelation
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookGlobalScriptRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookLocalScriptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybookDao {

    @Query("DELETE FROM playbook_local_scripts WHERE playbookId = :playbookId")
    suspend fun deleteLocalScripts(playbookId: String)

    @Query("DELETE FROM playbook_global_scripts WHERE playbookId = :playbookId")
    suspend fun deleteGlobalScripts(playbookId: String)

    @Transaction
    suspend fun deleteAllPlaybookScripts(playbookId: String) {
        deleteLocalScripts(playbookId)
        deleteGlobalScripts(playbookId)
    }

    @Query("DELETE FROM playbooks WHERE playbookId = :playbookId")
    suspend fun deletePlaybook(playbookId: String)

    @Query("SELECT * FROM playbooks WHERE playbookId = :playbookId")
    suspend fun getPlaybook(playbookId: String): PlaybookEntity?

    @Query("SELECT * FROM playbooks")
    fun getPlaybooks(): Flow<List<PlaybookEntity>>

    @Query("""SELECT * FROM playbook_local_scripts WHERE playbookId = :playbookId""")
    suspend fun getPlaybookLocalScripts(
        playbookId: String
    ): List<PlaybookLocalScriptEntity>

    @Transaction
    @Query(
        """
        SELECT * FROM playbook_global_scripts
        WHERE playbookId = :playbookId
        ORDER BY `order`
    """
    )
    suspend fun getPlaybookGlobalScripts(
        playbookId: String
    ): List<PlaybookGlobalScriptRelation>

    @Query(
        """
        SELECT * FROM devices d
        JOIN playbook_device_relations pd ON d.deviceId = pd.deviceId
        WHERE pd.playbookId = :playbookId
    """
    )
    suspend fun getPlaybookDevices(
        playbookId: String
    ): List<DeviceEntity>

    @Query(
        """
                SELECT * FROM device_groups d
        JOIN playbook_device_group_relations pd ON d.groupId = pd.groupId
        WHERE pd.playbookId = :playbookId
    """
    )
    suspend fun getPlaybookDeviceGroups(
        playbookId: String
    ): List<DeviceGroupEntity>

    @Transaction
    suspend fun getFullPlaybookRelation(
        playbookId: String
    ): FullPlaybookRelation {
        val playbook = getPlaybook(playbookId) ?: error("playbookId=$playbookId not found")
        val localScripts = getPlaybookLocalScripts(playbookId)
        val globalScripts = getPlaybookGlobalScripts(playbookId)
        val devices = getPlaybookDevices(playbookId)
        val deviceGroups = getPlaybookDeviceGroups(playbookId)

        return FullPlaybookRelation(
            playbook = playbook,
            localScripts = localScripts,
            globalScripts = globalScripts,
            devices = devices,
            deviceGroups = deviceGroups
        )
    }


//    @Transaction
//    @Query("SELECT * FROM playbooks")
//    fun getPlaybooksWithActions(): Flow<List<PlaybookWithActionsAndDevicesRelation>>

    @Upsert
    suspend fun insertLocalScripts(scripts: List<PlaybookLocalScriptEntity>)

    @Upsert
    suspend fun insertGlobalScripts(relations: List<PlaybookGlobalScriptRelationEntity>)

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