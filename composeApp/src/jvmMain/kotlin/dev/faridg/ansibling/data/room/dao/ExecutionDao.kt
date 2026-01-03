package dev.faridg.ansibling.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import dev.faridg.ansibling.data.room.entity.execution.ExecutionEntity
import dev.faridg.ansibling.data.room.entity.execution.ExecutionOutputEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExecutionDao {
    @Upsert
    suspend fun insertExecution(executionEntity: ExecutionEntity) : Long
//    @Query("DELETE FROM playbook_executions WHERE playbookId = :playbookId")
//    suspend fun deleteExecution(playbookId: String)
    @Query("SELECT * FROM executions WHERE executionId = :executionId")
    fun observeExecution(executionId: Long) : Flow<ExecutionEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExecutionOutput(output: ExecutionOutputEntity)
//    @Query("DELETE FROM execution_outputs WHERE executionId = :executionId")
//    suspend fun deleteExecutionOutput(executionId: Long)
    @Query("SELECT * FROM execution_outputs WHERE executionId = :executionId")
    fun observeOutputs(executionId: Long) : Flow<List<ExecutionOutputEntity>>
}