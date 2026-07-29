package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collaboratorId: Long,
    val collaboratorNome: String,
    val pix: String,
    val banco: String,
    val valor: Double,
    val ajudaDeCusto: Double = 0.0,
    val tipo: String = "Diária", // Diária, Semanal, Evento, Reembolso
    val situacao: String = "Pago", // Pendente, Pago, Cancelado
    val dataPagamento: Long = System.currentTimeMillis(),
    val reciboHash: String = "",
    val observacoes: String = ""
)
