package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material_deliveries")
data class MaterialDelivery(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collaboratorId: Long,
    val collaboratorNome: String,
    val camiseta: Int = 0,
    val bone: Int = 0,
    val bandeira: Int = 0,
    val adesivo: Int = 0,
    val praguinha: Int = 0,
    val santinhos: Int = 0,
    val perfuraco: Int = 0,
    val manual: Int = 0,
    val dataEntrega: Long = System.currentTimeMillis(),
    val entreguePor: String = "Logística Central"
)
