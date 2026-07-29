package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class CampaignEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val tipo: String, // Reunião, Carreata, Bandeiraço, Visita, Comício
    val dataHora: Long = System.currentTimeMillis(),
    val local: String,
    val bairro: String = "",
    val responsavel: String,
    val descricao: String = "",
    val listaPresencaCount: Int = 0,
    val status: String = "Agendado" // Agendado, Em Andamento, Concluído, Cancelado
)
