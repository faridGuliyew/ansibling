package dev.faridg.ansibling.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {

    @Upsert
    suspend fun insert(script: ScriptEntity)

    @Query("DELETE FROM scripts WHERE scriptId = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM scripts")
    fun observeAll(): Flow<List<ScriptEntity>>

}