package com.example.pasteleriamilsabores

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.ui.screens.ProductListScreen
import com.example.pasteleriamilsabores.ui.theme.PasteleriaMilSabores
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var repository: Repository
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        coEvery { repository.getAllProducts() } returns emptyList()

        productViewModel = ProductViewModel(repository)
        cartViewModel = CartViewModel(repository)
        authViewModel = AuthViewModel(repository)
    }

    @Test
    fun productListScreen_displaysButtons() {
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
                    onNavigateToProfile = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Agregar").assertExists()
        composeTestRule.onNodeWithText("Cargar API").assertExists()
        composeTestRule.onNodeWithText("Carrito").assertExists()
    }

    @Test
    fun productListScreen_addProductButton_isClickable() {
        // Given
        var addProductClicked = false

        composeTestRule.setContent {
            PasteleriaMilSabores {
                ProductListScreen(
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel,
                    onNavigateToAddProduct = { addProductClicked = true },
                    onNavigateToEditProduct = {},
                    onNavigateToCart = {},
                    onNavigateToProfile = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithText("Agregar").performClick()

        // Then
        assert(addProductClicked)
    }

    @Test
    fun productListScreen_profileIcon_isClickable() {
        // Given
        var profileClicked = false

        composeTestRule.setContent {
            PasteleriaMilSabores {
                ProductListScreen(
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel,
                    onNavigateToAddProduct = {},
                    onNavigateToEditProduct = {},
                    onNavigateToCart = {},
                    onNavigateToProfile = { profileClicked = true }
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Perfil").performClick()

        // Then
        assert(profileClicked)
    }
}