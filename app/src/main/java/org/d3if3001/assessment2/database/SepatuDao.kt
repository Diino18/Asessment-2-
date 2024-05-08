package org.d3if3001.assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3001.assessment2.model.Sepatu

@Dao
interface SepatuDao {
    @Insert
    suspend fun insert(sepatu: Sepatu)

    @Update
    suspend fun update(sepatu: Sepatu)

    @Query("SELECT * FROM sepatu ORDER BY nama ASC")
    fun getSepatu(): Flow<List<Sepatu>>

    @Query("SELECT * FROM sepatu WHERE id = :id")
    suspend fun getSepatuById(id: Long): Sepatu

    @Query("DELETE FROM sepatu WHERE id = :id")
    suspend fun deleteById(id: Long)
}