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

        val productoRepository = ProductoRepository()


        if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistroViewModel(usuarioRepository) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(usuarioRepository) as T
        }

        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogoViewModel(productoRepository) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
    }