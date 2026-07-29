package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderships")
data class Leadership(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val bairro: String,
    val comunidade: String = "",
    val nivelInfluencia: String = "Alta", // Alta, Média, Estratégica
    val contato: String,
    val familiasImpactadas: Int = 50,
    val apoiadoresGarantidos: Int = 120,
    val segmento: String = "Associação de Bairro",
    val observacoes: String = ""
)
