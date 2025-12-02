package com.example.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.model.User
import com.example.pasteleriamilsabores.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        if (!isValidEmail(email)) {
            _loginState.value = LoginState.Error("Email inválido")
            return
        }
        if (password.length < 6) {
            _loginState.value = LoginState.Error("Contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val user = repository.login(email, password)
            if (user != null) {
                _currentUser.value = user
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Credenciales incorrectas")
            }
        }
    }

    fun register(email: String, password: String, name: String, phone: String?) {
        if (!isValidEmail(email)) {
            _loginState.value = LoginState.Error("Email inválido")
            return
        }
        if (password.length < 6) {
            _loginState.value = LoginState.Error("Contraseña debe tener al menos 6 caracteres")
            return
        }
        if (name.isBlank()) {
            _loginState.value = LoginState.Error("Nombre requerido")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val userId = repository.register(email, password, name, phone)
            if (userId > 0) {
                _currentUser.value = User(userId, email, name, phone)
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Error al registrar usuario")
            }
        }
    }

    fun updateProfile(name: String, phone: String?) {
        val user = _currentUser.value ?: return

        if (name.isBlank()) {
            _loginState.value = LoginState.Error("Nombre requerido")
            return
        }

        viewModelScope.launch {
            val success = repository.updateUser(user.id, name, phone)
            if (success) {
                _currentUser.value = user.copy(name = name, phone = phone)
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Error al actualizar perfil")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _loginState.value = LoginState.Idle
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}