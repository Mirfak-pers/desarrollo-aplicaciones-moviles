package com.example.pasteleriamilsabores.data.repository

import com.example.pasteleriamilsabores.data.local.DatabaseHelper
import com.example.pasteleriamilsabores.data.model.CartItem
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.model.User
import com.example.pasteleriamilsabores.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val db: DatabaseHelper,
    private val api: ApiService
) {
    suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        db.getUser(email, password)
    }

    suspend fun register(email: String, password: String, name: String, phone: String?): Long =
        withContext(Dispatchers.IO) {
            db.insertUser(email, password, name, phone)
        }

    suspend fun updateUser(userId: Long, name: String, phone: String?): Boolean =
        withContext(Dispatchers.IO) {
            db.updateUser(userId, name, phone) > 0
        }

    suspend fun fetchProductsFromApi(): List<Product> = withContext(Dispatchers.IO) {
        api.getProducts().map {
            Product(
                id = it.id.toLong(),
                title = it.title,
                price = it.price,
                description = it.description,
                image = it.image
            )
        }
    }

    suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        db.getAllProducts()
    }

    suspend fun addProduct(title: String, price: Double, description: String?,
                           image: String?, userId: Long): Long = withContext(Dispatchers.IO) {
        db.insertProduct(title, price, description, image, userId)
    }

    suspend fun updateProduct(id: Long, title: String, price: Double,
                              description: String?, image: String?): Boolean =
        withContext(Dispatchers.IO) {
            db.updateProduct(id, title, price, description, image) > 0
        }

    suspend fun deleteProduct(id: Long): Boolean = withContext(Dispatchers.IO) {
        db.deleteProduct(id) > 0
    }

    suspend fun addToCart(userId: Long, productId: Long, quantity: Int): Long =
        withContext(Dispatchers.IO) {
            db.addToCart(userId, productId, quantity)
        }

    suspend fun getCartItems(userId: Long): List<CartItem> = withContext(Dispatchers.IO) {
        db.getCartItems(userId)
    }

    suspend fun removeFromCart(cartItemId: Long): Boolean = withContext(Dispatchers.IO) {
        db.removeFromCart(cartItemId) > 0
    }
}