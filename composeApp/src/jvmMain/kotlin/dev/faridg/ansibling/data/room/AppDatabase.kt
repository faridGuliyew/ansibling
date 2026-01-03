package dev.faridg.ansibling.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.faridg.ansibling.data.room.dao.ExecutionDao
import dev.faridg.ansibling.data.room.dao.DeviceDao
import dev.faridg.ansibling.data.room.dao.PlaybookDao
import dev.faridg.ansibling.data.room.dao.ScriptDao
import dev.faridg.ansibling.data.room.dao.VariableDao
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookScriptEntity
import dev.faridg.ansibling.data.room.entity.execution.ExecutionEntity
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.execution.ExecutionOutputEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookDeviceGroupRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookDeviceRelationEntity
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookEntity
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity
import dev.faridg.ansibling.data.room.entity.variable.VariableEntity
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        DeviceEntity::class, DeviceGroupEntity::class, DeviceGroupRelationEntity::class,
        ExecutionEntity::class,
        ExecutionOutputEntity::class,
        VariableEntity::class,
        ScriptEntity::class,
        PlaybookEntity::class,
        PlaybookScriptEntity::class,
        PlaybookDeviceRelationEntity::class, PlaybookDeviceGroupRelationEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val deviceDao: DeviceDao
    abstract val executionDao: ExecutionDao
    abstract val variableDao: VariableDao
    abstract val scriptDao: ScriptDao
    abstract val playbookDao: PlaybookDao

    companion object {
        private val appDatabase = Room.databaseBuilder<AppDatabase>(name = "appDatabase")
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()

        val deviceDao = appDatabase.deviceDao
        val playbookExecutionDao = appDatabase.executionDao
        val variableDao = appDatabase.variableDao
        val scriptDao = appDatabase.scriptDao
        val playbookDao = appDatabase.playbookDao
    }
}