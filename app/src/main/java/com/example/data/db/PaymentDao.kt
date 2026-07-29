package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PaymentRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY dataPagamento DESC")
    fun getAll(): Flow<List<PaymentRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PaymentRecord): Long

    @Query("SELECT SUM(valor + ajudaDeCusto) FROM payments WHERE situacao = 'Pago'")
    fun getTotalSpent(): Flow<Double?>
}
