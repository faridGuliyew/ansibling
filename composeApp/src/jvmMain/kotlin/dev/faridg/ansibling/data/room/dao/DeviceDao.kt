package dev.faridg.ansibling.data.room.dao

import androidx.room.*
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupWithDevicesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    /** DEVICE METHODS */
    @Upsert
    suspend fun insertDevice(entity: DeviceEntity)

    @Query("DELETE FROM devices WHERE deviceId = :id")
    suspend fun deleteDeviceById(id: String)

    @Query("SELECT * FROM devices")
    fun getAllDevicesAsFlow(): Flow<List<DeviceEntity>>

    /** DEVICE GROUP METHODS */
    @Upsert
    suspend fun insertDeviceGroup(entity: DeviceGroupEntity)

    @Query("INSERT INTO device_group_relations VALUES (:deviceId, :groupId)")
    suspend fun addDeviceIntoDeviceGroup(groupId: String, deviceId: String)

    @Query("DELETE FROM device_group_relations WHERE deviceId = :deviceId AND groupId = :groupId")
    suspend fun removeDeviceFromDeviceGroup(groupId: String, deviceId: String)

    @Query("DELETE FROM device_group_relations WHERE groupId = :groupId")
    suspend fun deleteAllDeviceGroupRelations(groupId: String)

    @Upsert
    suspend fun insertDeviceRelations(relations: List<DeviceGroupRelationEntity>)

    @Query("DELETE FROM device_groups WHERE groupId = :id")
    suspend fun deleteDeviceGroupById(id: String)

    @Transaction
    @Query("SELECT * FROM device_groups")
    fun getAllDeviceGroupsWithDevicesAsFlow(): Flow<List<DeviceGroupWithDevicesRelation>>

    @Transaction
    @Query("SELECT * FROM device_groups WHERE groupId = :groupId")
    suspend fun getAllDevicesForGroup(groupId: String): DeviceGroupWithDevicesRelation

    /** OTHER METHODS */
//    @Insert
//    suspend fun insertActions(entities: List<RemoteActionEntity>)

//    @Transaction
//    suspend fun updateActions(item: Device, actions: List<RemoteAction>) {
//        deleteRemoteActionsForPlaybook(item.id)
//        insertActions(actions.map { it.toEntity(item.id) })
//    }

}