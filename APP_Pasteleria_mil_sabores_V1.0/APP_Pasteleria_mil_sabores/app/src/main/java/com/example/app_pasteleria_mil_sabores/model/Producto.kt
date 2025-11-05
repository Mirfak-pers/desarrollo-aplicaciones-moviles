package com.example.app_pasteleria_mil_sabores.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String = "Delicioso producto de la pastelería."
)