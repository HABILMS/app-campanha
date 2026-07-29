package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Leadership
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadershipDao {
    @Query("SELECT * FROM leaderships ORDER BY apoiadoresGarantidos DESC")
    fun getAll(): Flow<List<Leadership>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(leadership: Leadership): Long
}
