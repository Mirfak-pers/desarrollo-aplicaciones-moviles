package com.example.pasteleriamilsabores.data.model

data class User(
    val id: Long = 0,
    val email: String,
    val name: String,
    val phone: String? = null
)

data class Product(
    val id: Long = 0,
    val title: String,
    val price: Double,
    val description: String? = null,
    val image: String? = null
)

data class CartItem(
    val id: Long = 0,
    val product: Product,
    val quantity: Int
)

data class ApiProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val category: String
)