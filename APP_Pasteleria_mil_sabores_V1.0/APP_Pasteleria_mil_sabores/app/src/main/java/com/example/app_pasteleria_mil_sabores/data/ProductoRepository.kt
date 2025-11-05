package com.example.app_pasteleria_mil_sabores.data


import com.example.app_pasteleria_mil_sabores.model.Producto
import kotlinx.coroutines.delay

class ProductoRepository {

    private val stockProductos = listOf(
        Producto("TC001", "Tortas Cuadradas", "Torta Cuadrada de Chocolate", 45000.0),
        Producto("TC002", "Tortas Cuadradas", "Torta Cuadrada de Frutas", 50000.0),
        Producto("TT001", "Tortas Circulares", "Torta Circular de Vainilla", 40000.0),
        Producto("TT002", "Tortas Circulares", "Torta Circular de Manjar", 42000.0),
        Producto("PI001", "Postres Individuales", "Mousse de Chocolate", 5000.0),
        Producto("PI002", "Postres Individuales", "Tiramisú Clásico", 5500.0),
        Producto("PSA001", "Productos Sin Azúcar", "Torta Sin Azúcar de Naranja", 48000.0),
        Producto("PSA002", "Productos Sin Azúcar", "Cheesecake Sin Azúcar", 47000.0),
        Producto("PT001", "Pastelería Tradicional", "Empanada de Manzana", 3000.0),
        Producto("PT002", "Pastelería Tradicional", "Tarta de Santiago", 6000.0),
        Producto("PG001", "Productos Sin Gluten", "Brownie Sin Gluten", 4000.0),
        Producto("PG002", "Productos Sin Gluten", "Pan Sin Gluten", 3500.0),
        Producto("PV001", "Productos Vegana", "Torta Vegana de Chocolate", 50000.0),
        Producto("PV002", "Productos Vegana", "Galletas Veganas de Avena", 4500.0),
        Producto("TE001", "Tortas Especiales", "Torta Especial de Cumpleaños", 55000.0),
        Producto("TE002", "Tortas Especiales", "Torta Especial de Boda", 60000.0)
    )

    suspend fun obtenerCatalogo(): List<Producto> {
        delay(500)
        return stockProductos
    }
}