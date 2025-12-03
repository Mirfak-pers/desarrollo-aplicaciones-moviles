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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("ProductViewModel Tests")
class ProductViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: ProductViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        coEvery { repository.getAllProducts() } returns emptyList()

        viewModel = ProductViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("loadProducts debe actualizar la lista de productos")
    fun loadProducts_shouldUpdateProductList() = runTest {
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
    @DisplayName("addProduct con título vacío debe retornar error")
    fun addProduct_withEmptyTitle_shouldReturnError() = runTest {
        // When
        viewModel.addProduct("", "10.0", "Descripción", "image.jpg", 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.productState.value is ProductViewModel.ProductState.Error)
        val errorState = viewModel.productState.value as ProductViewModel.ProductState.Error
        assertEquals("Título requerido", errorState.message)
    }

    @Test
    @DisplayName("addProduct con precio inválido debe retornar error")
    fun addProduct_withInvalidPrice_shouldReturnError() = runTest {
        // When
        viewModel.addProduct("Producto", "abc", "Descripción", "image.jpg", 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.productState.value is ProductViewModel.ProductState.Error)
        val errorState = viewModel.productState.value as ProductViewModel.ProductState.Error
        assertEquals("Precio inválido", errorState.message)
    }

    @Test
    @DisplayName("addProduct con datos válidos debe llamar al repositorio")
    fun addProduct_withValidData_shouldCallRepository() = runTest {
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
    @DisplayName("deleteProduct debe llamar al repositorio y recargar productos")
    fun deleteProduct_shouldCallRepositoryAndReloadProducts() = runTest {
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
    @DisplayName("loadProductsFromApi debe actualizar productos desde la API")
    fun loadProductsFromApi_shouldUpdateProductsFromApi() = runTest {
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
    @DisplayName("loadProductsFromApi con error debe actualizar estado a Error")
    fun loadProductsFromApi_withError_shouldUpdateStateToError() = runTest {
        // Given
        coEvery { repository.fetchProductsFromApi() } throws Exception("Network error")

        // When
        viewModel.loadProductsFromApi()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.productState.value is ProductViewModel.ProductState.Error)
    }

    @Test
    @DisplayName("updateProduct con datos válidos debe actualizar el producto")
    fun updateProduct_withValidData_shouldUpdateProduct() = runTest {
        // Given
        coEvery { repository.updateProduct(any(), any(), any(), any(), any()) } returns true
        coEvery { repository.getAllProducts() } returns emptyList()

        // When
        viewModel.updateProduct(1L, "Updated", "25.0", "New desc", "new.jpg")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.updateProduct(1L, "Updated", 25.0, "New desc", "new.jpg") }
    }
}