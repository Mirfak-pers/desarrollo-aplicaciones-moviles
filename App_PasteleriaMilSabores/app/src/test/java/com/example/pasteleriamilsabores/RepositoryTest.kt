package com.example.pasteleriamilsabores


import com.example.pasteleriamilsabores.data.local.DatabaseHelper
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.model.User
import com.example.pasteleriamilsabores.data.remote.ApiService
import com.example.pasteleriamilsabores.data.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class RepositoryTest {

    private lateinit var db: DatabaseHelper
    private lateinit var api: ApiService
    private lateinit var repository: Repository

    @Before
    fun setup() {
        db = mockk()
        api = mockk()
        repository = Repository(db, api)
    }

    @Test
    fun `login debe retornar usuario cuando las credenciales son correctas`() = runTest {
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
    fun `register debe retornar el ID del nuevo usuario`() = runTest {
        // Given
        every { db.insertUser(any(), any(), any(), any()) } returns 1L

        // When
        val result = repository.register("new@test.com", "password", "New User", null)

        // Then
        assertEquals(1L, result)
        verify { db.insertUser("new@test.com", "password", "New User", null) }
    }

    @Test
    fun `getAllProducts debe retornar lista de productos`() = runTest {
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
    fun `addProduct debe retornar el ID del nuevo producto`() = runTest {
        // Given
        every { db.insertProduct(any(), any(), any(), any(), any()) } returns 1L

        // When
        val result = repository.addProduct("New Product", 15.0, "Description", "image.jpg", 1L)

        // Then
        assertEquals(1L, result)
        verify { db.insertProduct("New Product", 15.0, "Description", "image.jpg", 1L) }
    }

    @Test
    fun `updateProduct debe retornar true cuando la actualización es exitosa`() = runTest {
        // Given
        every { db.updateProduct(any(), any(), any(), any(), any()) } returns 1

        // When
        val result = repository.updateProduct(1L, "Updated", 25.0, "New desc", "new.jpg")

        // Then
        assertTrue(result)
        verify { db.updateProduct(1L, "Updated", 25.0, "New desc", "new.jpg") }
    }

    @Test
    fun `deleteProduct debe retornar true cuando la eliminación es exitosa`() = runTest {
        // Given
        every { db.deleteProduct(1L) } returns 1

        // When
        val result = repository.deleteProduct(1L)

        // Then
        assertTrue(result)
        verify { db.deleteProduct(1L) }
    }

    @Test
    fun `addToCart debe retornar el ID del item agregado`() = runTest {
        // Given
        every { db.addToCart(any(), any(), any()) } returns 1L

        // When
        val result = repository.addToCart(1L, 1L, 2)

        // Then
        assertEquals(1L, result)
        verify { db.addToCart(1L, 1L, 2) }
    }
}