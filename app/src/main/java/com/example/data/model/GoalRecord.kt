package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collaboratorId: Long,
    val collaboratorNome: String,
    val metaVisitas: Int = 50,
    val realizadasVisitas: Int = 42,
    val metaCadastros: Int = 20,
    val realizadasCadastros: Int = 18,
    val metaReunioes: Int = 5,
    val realizadasReunioes: Int = 4,
    val metaEventos: Int = 3,
    val realizadasEventos: Int = 3,
    val metaMaterial: Int = 500,
    val realizadasMaterial: Int = 480,
    val metaApoiadores: Int = 15,
    val realizadasApoiadores: Int = 14,
    val periodo: String = "Semanal" // Diária, Semanal, Mensal
) {
    val percentualConclusao: Int
        get() {
            val totalMeta = metaVisitas + metaCadastros + metaReunioes + metaEventos + metaApoiadores
            val totalRealizado = realizadasVisitas + realizadasCadastros + realizadasReunioes + realizadasEventos + realizadasApoiadores
            return if (totalMeta > 0) ((totalRealizado.toDouble() / totalMeta.toDouble()) * 100).toInt().coerceIn(0, 100) else 0
        }
}
