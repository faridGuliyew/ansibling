package dev.faridg.ansibling.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.faridg.ansibling.data.room.entity.variable.VariableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VariableDao {

    @Upsert
    suspend fun insert(variable: VariableEntity)

    @Query("DELETE FROM variables WHERE variableId = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM variables")
    fun observeAll(): Flow<List<VariableEntity>>

}