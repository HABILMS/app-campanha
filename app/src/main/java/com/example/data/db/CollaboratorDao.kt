package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Collaborator
import kotlinx.coroutines.flow.Flow

@Dao
interface CollaboratorDao {
    @Query("SELECT * FROM collaborators ORDER BY id DESC")
    fun getAll(): Flow<List<Collaborator>>

    @Query("SELECT * FROM collaborators WHERE id = :id")
    suspend fun getById(id: Long): Collaborator?

    @Query("SELECT * FROM collaborators WHERE cpf = :cpf LIMIT 1")
    suspend fun getByCpf(cpf: String): Collaborator?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collaborator: Collaborator): Long

    @Update
    suspend fun update(collaborator: Collaborator)

    @Query("DELETE FROM collaborators WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM collaborators")
    fun countTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM collaborators WHERE status = 'Ativo'")
    fun countActive(): Flow<Int>
}
