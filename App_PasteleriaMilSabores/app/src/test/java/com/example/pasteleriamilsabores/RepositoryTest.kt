package com.example.pasteleriamilsabores


import com.example.pasteleriamilsabores.data.local.DatabaseHelper
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.model.User
import com.example.pasteleriamilsabores.data.remote.ApiService
import com.example.pasteleriamilsabores.data.repository.Repository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

@DisplayName("Repository Tests")
class RepositoryTest {

    private lateinit var db: DatabaseHelper
    private lateinit var api: ApiService
    private lateinit var repository: Repository

    @BeforeEach
    fun setup() {
        db = mockk()
        api = mockk()
        repository = Repository(db, api)
    }

    @Test
    @DisplayName("login debe retornar usuario cuando las credenciales son correctas")
    fun login_withCorrectCredentials_shouldReturnUser() = runTest {
        // Given
        val expectedUser = User(1, "test@test.com", "Test User")
        every { db.getUser("test@test.com", "password") } returns expectedUser

        // When
        val result = repository.login("test@test.com", "password")

        // Then
        assertEquals(expectedUser, result)
        verify { db.getUser("test@test.com", "password") }
    }

    @Test
    @DisplayName("register debe retornar el ID del nuevo usuario")
    fun register_shouldReturnNewUserId() = runTest {
        // Given
        every { db.insertUser(any(), any(), any(), any()) } returns 1L

        // When
        val result = repository.register("new@test.com", "password", "New User", null)

        // Then
        assertEquals(1L, result)
        verify { db.insertUser("new@test.com", "password", "New User", null) }
    }

    @Test
    @DisplayName("getAllProducts debe retornar lista de productos")
    fun getAllProducts_shouldReturnProductList() = runTest {
        // Given
        val products = listOf(
            Product(1, "Product 1", 10.0),
            Product(2, "Product 2", 20.0)
        )
        every { db.getAllProducts() } returns products

        // When
        val result = repository.getAllProducts()

        // Then
        assertEquals(products, result)
        verify { db.getAllProducts() }
    }

    @Test
    @DisplayName("addProduct debe retornar el ID del nuevo producto")
    fun addProduct_shouldReturnNewProductId() = runTest {
        // Given
        every { db.insertProduct(any(), any(), any(), any(), any()) } returns 1L

        // When
        val result = repository.addProduct("New Product", 15.0, "Description", "image.jpg", 1L)

        // Then
        assertEquals(1L, result)
        verify { db.insertProduct("New Product", 15.0, "Description", "image.jpg", 1L) }
    }

    @Test
    @DisplayName("updateProduct debe retornar true cuando la actualización es exitosa")
    fun updateProduct_whenSuccessful_shouldReturnTrue() = runTest {
        // Given
        every { db.updateProduct(any(), any(), any(), any(), any()) } returns 1

        // When
        val result = repository.updateProduct(1L, "Updated", 25.0, "New desc", "new.jpg")

        // Then
        assertTrue(result)
        verify { db.updateProduct(1L, "Updated", 25.0, "New desc", "new.jpg") }
    }

    @Test
    @DisplayName("deleteProduct debe retornar true cuando la eliminación es exitosa")
    fun deleteProduct_whenSuccessful_shouldReturnTrue() = runTest {
        // Given
        every { db.deleteProduct(1L) } returns 1

        // When
        val result = repository.deleteProduct(1L)

        // Then
        assertTrue(result)
        verify { db.deleteProduct(1L) }
    }

    @Test
    @DisplayName("addToCart debe retornar el ID del item agregado")
    fun addToCart_shouldReturnCartItemId() = runTest {
        // Given
        every { db.addToCart(any(), any(), any()) } returns 1L

        // When
        val result = repository.addToCart(1L, 1L, 2)

        // Then
        assertEquals(1L, result)
        verify { db.addToCart(1L, 1L, 2) }
    }
}