package com.example.pasteleriamilsabores


import com.example.pasteleriamilsabores.data.model.User
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
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
class AuthViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login con credenciales válidas debe actualizar currentUser`() = runTest {
        // Given
        val expectedUser = User(1, "test@test.com", "Test User")
        coEvery { repository.login("test@test.com", "password123") } returns expectedUser

        // When
        viewModel.login("test@test.com", "password123")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(expectedUser, viewModel.currentUser.value)
        assertEquals(AuthViewModel.LoginState.Success, viewModel.loginState.value)
    }

    @Test
    fun `login con credenciales inválidas debe retornar error`() = runTest {
        // Given
        coEvery { repository.login("test@test.com", "wrong") } returns null

        // When
        viewModel.login("test@test.com", "wrong")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.currentUser.value)
        assertTrue(viewModel.loginState.value is AuthViewModel.LoginState.Error)
    }

    @Test
    fun `login con email inválido debe retornar error`() = runTest {
        // When
        viewModel.login("invalid-email", "password123")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.loginState.value is AuthViewModel.LoginState.Error)
        val errorState = viewModel.loginState.value as AuthViewModel.LoginState.Error
        assertEquals("Email inválido", errorState.message)
    }

    @Test
    fun `login con contraseña corta debe retornar error`() = runTest {
        // When
        viewModel.login("test@test.com", "123")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.loginState.value is AuthViewModel.LoginState.Error)
        val errorState = viewModel.loginState.value as AuthViewModel.LoginState.Error
        assertEquals("Contraseña debe tener al menos 6 caracteres", errorState.message)
    }

    @Test
    fun `register con datos válidos debe crear usuario`() = runTest {
        // Given
        coEvery { repository.register(any(), any(), any(), any()) } returns 1L

        // When
        viewModel.register("new@test.com", "password123", "New User", "123456789")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.currentUser.value)
        assertEquals("new@test.com", viewModel.currentUser.value?.email)
        coVerify { repository.register("new@test.com", "password123", "New User", "123456789") }
    }

    @Test
    fun `register con nombre vacío debe retornar error`() = runTest {
        // When
        viewModel.register("test@test.com", "password123", "", null)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.loginState.value is AuthViewModel.LoginState.Error)
        val errorState = viewModel.loginState.value as AuthViewModel.LoginState.Error
        assertEquals("Nombre requerido", errorState.message)
    }

    @Test
    fun `updateProfile debe actualizar los datos del usuario`() = runTest {
        // Given
        val initialUser = User(1, "test@test.com", "Old Name", "111")
        viewModel.login("test@test.com", "password")
        coEvery { repository.login(any(), any()) } returns initialUser
        coEvery { repository.updateUser(1L, "New Name", "222") } returns true

        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.updateProfile("New Name", "222")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("New Name", viewModel.currentUser.value?.name)
        assertEquals("222", viewModel.currentUser.value?.phone)
    }

    @Test
    fun `logout debe limpiar currentUser`() = runTest {
        // Given
        val user = User(1, "test@test.com", "Test User")
        coEvery { repository.login(any(), any()) } returns user
        viewModel.login("test@test.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.logout()

        // Then
        assertNull(viewModel.currentUser.value)
        assertEquals(AuthViewModel.LoginState.Idle, viewModel.loginState.value)
    }
}