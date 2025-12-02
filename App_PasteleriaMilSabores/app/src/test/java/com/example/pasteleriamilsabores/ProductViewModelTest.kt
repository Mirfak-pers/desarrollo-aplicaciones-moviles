package com.example.pasteleriamilsabores


import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: ProductViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        // Mock inicial para el constructor
        coEvery { repository.getAllProducts() } returns emptyList()

        viewModel = ProductViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProducts debe actualizar la lista de productos`() = runTest {
        // Given
        val expectedProducts = listOf(
            Product(1, "Producto 1", 10.0, "Desc 1"),
            Product(2, "Producto 2", 20.0, "Desc 2")
        )
        coEvery { repository.getAllProducts() } returns expectedProducts

        // When
        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(expectedProducts, viewModel.products.value)
        assertEquals(ProductViewModel.ProductState.Success, viewModel.productState.value)
    }

    @Test
    fun `addProduct con título vacío debe retornar error`() = runTest {
        // When
        viewModel.addProduct("", "10.0", "Descripción", "image.jpg", 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.productState.value is ProductViewModel.ProductState.Error)
        val errorState = viewModel.productState.value as ProductViewModel.ProductState.Error
        assertEquals("Título requerido", errorState.message)
    }

    @Test
    fun `addProduct con precio inválido debe retornar error`() = runTest {
        // When
        viewModel.addProduct("Producto", "abc", "Descripción", "image.jpg", 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.productState.value is ProductViewModel.ProductState.Error)
        val errorState = viewModel.productState.value as ProductViewModel.ProductState.Error
        assertEquals("Precio inválido", errorState.message)
    }

    @Test
    fun `addProduct con datos válidos debe llamar al repositorio`() = runTest {
        // Given
        coEvery { repository.addProduct(any(), any(), any(), any(), any()) } returns 1L
        coEvery { repository.getAllProducts() } returns emptyList()

        // When
        viewModel.addProduct("Producto", "10.0", "Descripción", "image.jpg", 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.addProduct("Producto", 10.0, "Descripción", "image.jpg", 1L) }
    }

    @Test
    fun `deleteProduct debe llamar al repositorio y recargar productos`() = runTest {
        // Given
        coEvery { repository.deleteProduct(1L) } returns true
        coEvery { repository.getAllProducts() } returns emptyList()

        // When
        viewModel.deleteProduct(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.deleteProduct(1L) }
        coVerify { repository.getAllProducts() }
    }

    @Test
    fun `loadProductsFromApi debe actualizar productos desde la API`() = runTest {
        // Given
        val apiProducts = listOf(
            Product(1, "API Product", 15.0, "From API")
        )
        coEvery { repository.fetchProductsFromApi() } returns apiProducts

        // When
        viewModel.loadProductsFromApi()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(apiProducts, viewModel.products.value)
        assertEquals(ProductViewModel.ProductState.Success, viewModel.productState.value)
    }

    @Test
    fun `loadProductsFromApi con error debe actualizar estado a Error`() = runTest {
        // Given
        coEvery { repository.fetchProductsFromApi() } throws Exception("Network error")

        // When
        viewModel.loadProductsFromApi()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.productState.value is ProductViewModel.ProductState.Error)
    }
}