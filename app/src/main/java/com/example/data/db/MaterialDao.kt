package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.MaterialDelivery
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Query("SELECT * FROM material_deliveries ORDER BY dataEntrega DESC")
    fun getAll(): Flow<List<MaterialDelivery>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(delivery: MaterialDelivery): Long
}
