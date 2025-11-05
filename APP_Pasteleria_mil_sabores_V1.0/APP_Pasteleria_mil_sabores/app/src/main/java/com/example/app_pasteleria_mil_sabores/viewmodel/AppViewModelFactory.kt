package com.example.app_pasteleria_mil_sabores.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_pasteleria_mil_sabores.data.AppDatabase
import com.example.app_pasteleria_mil_sabores.data.ProductoRepository
import com.example.app_pasteleria_mil_sabores.data.UsuarioRepository
import com.example.app_pasteleria_mil_sabores.ui.catalog.CatalogoViewModel
import com.example.app_pasteleria_mil_sabores.ui.login.LoginViewModel
import com.example.app_pasteleria_mil_sabores.ui.register.RegistroViewModel

class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val database = AppDatabase.getDatabase(application)

        val usuarioRepository = UsuarioRepository(database.usuarioDao())

        // 2. Repositorio de Productos (usa datos 'mocked' por ahora)
        val productoRepository = ProductoRepository()

        // --- Lógica de creación por tipo de ViewModel ---

        // A. Registro ViewModel
        if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistroViewModel(usuarioRepository) as T
        }

        // B. Login ViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(usuarioRepository) as T
        }

        // C. Catálogo ViewModel
        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogoViewModel(productoRepository) as T
        }

        // D. Perfil ViewModel (Futuro)
        // Aquí iría la lógica para PerfilViewModel(usuarioRepository)

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
    }