package com.example.pasteleriamilsabores

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.ui.screens.LoginScreen
import com.example.pasteleriamilsabores.ui.theme.PasteleriaMilSabores
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var repository: Repository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun loginScreen_displaysAllElements() {
        // When
        composeTestRule.setContent {
            PasteleriaMilSabores {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = {},
                    onLoginSuccess = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Iniciar Sesión").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Contraseña").assertExists()
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate").assertExists()
    }

    @Test
    fun loginScreen_emailInput_acceptsText() {
        // Given
        composeTestRule.setContent {
            PasteleriaMilSabores {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = {},
                    onLoginSuccess = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithText("Email").performTextInput("test@test.com")

        // Then
        composeTestRule.onNodeWithText("test@test.com").assertExists()
    }

    @Test
    fun loginScreen_passwordInput_acceptsText() {
        // Given
        composeTestRule.setContent {
            PasteleriaMilSabores {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = {},
                    onLoginSuccess = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")

        // Then
        composeTestRule.onNodeWithText("Contraseña").assertExists()
    }

    @Test
    fun loginScreen_loginButton_isClickable() {
        // Given
        composeTestRule.setContent {
            PasteleriaMilSabores {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = {},
                    onLoginSuccess = {}
                )
            }
        }

        // When & Then
        composeTestRule.onNodeWithText("Iniciar Sesión")
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun loginScreen_registerButton_isClickable() {
        // Given
        var registerClicked = false

        composeTestRule.setContent {
            PasteleriaMilSabores {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = { registerClicked = true },
                    onLoginSuccess = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate").performClick()

        // Then
        assert(registerClicked)
    }
}