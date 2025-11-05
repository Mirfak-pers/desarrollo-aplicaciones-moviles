package com.example.app_pasteleria_mil_sabores.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.UsuarioRepository
import com.example.app_pasteleria_mil_sabores.model.Usuario
import kotlinx.coroutines.launch

class RegistroViewModel(private val repository: UsuarioRepository) : ViewModel() {

    val nombre = MutableLiveData("")
    val contrasena = MutableLiveData("")
    val registroExitoso = MutableLiveData(false)
    val mensajeError = MutableLiveData<String?>(null)


    fun registrarUsuario() {
        val nombreUsuario = nombre.value ?: ""
        val contrasenaUsuario = contrasena.value ?: ""

        if (nombreUsuario.isBlank() || contrasenaUsuario.isBlank()) {
            mensajeError.value = "Por favor, complete ambos campos."
            return
        }

        viewModelScope.launch {

            val nuevoUsuario = Usuario(nombre = nombreUsuario, contrasena = contrasenaUsuario)


            repository.registrarUsuario(nuevoUsuario)


            nombre.value = ""
            contrasena.value = ""
            registroExitoso.value = true
        }
    }


    fun limpiarEstadoExito() {
        registroExitoso.value = false
    }

    fun limpiarMensajes() {
        mensajeError.value = null
    }
}