package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collaboratorId: Long,
    val collaboratorNome: String,
    val tipo: String = "Entrada", // Entrada, Saída
    val horaRegistro: Long = System.currentTimeMillis(),
    val evento: String = "",
    val bairro: String = "",
    val atividade: String = "Panfletagem & Mobilização",
    val observacoes: String = "",
    val photoUri: String = "",
    val latitude: Double = -23.55052,
    val longitude: Double = -46.633308,
    val registradoPor: String = "Coordenador Regional"
)
