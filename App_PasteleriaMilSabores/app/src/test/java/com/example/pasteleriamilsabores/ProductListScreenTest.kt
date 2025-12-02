package com.example.pasteleriamilsabores


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.model.User
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.ui.screens.ProductListScreen
import com.example.pasteleriamilsabores.ui.theme.PasteleriaMilSabores
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ProductListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productListScreen_displaysButtons() {
        // Given
        val repository: Repository = mockk(relaxed = true)
        coEvery { repository.getAllProducts() } returns emptyList()

        val productViewModel = ProductViewModel(repository)
        val cartViewModel = CartViewModel(repository)
        val authViewModel = AuthViewModel(repository)

        // When
        composeTestRule.setContent {
            PasteleriaMilSabores {
                ProductListScreen(
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel,
                    onNavigateToAddProduct = {},
                    onNavigateToEditProduct = {},
                    onNavigateToCart = {},
                    onNavigateToProfile = {} // NUEVO
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Agregar").assertExists()
        composeTestRule.onNodeWithText("Cargar API").assertExists()
        composeTestRule.onNodeWithText("Carrito").assertExists()
    }
}