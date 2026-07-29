package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_logs")
data class AuditLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val usuario: String,
    val perfil: String,
    val acao: String,
    val detalhe: String,
    val ipAddress: String = "192.168.1.45"
)
