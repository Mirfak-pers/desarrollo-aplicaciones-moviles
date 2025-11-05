package com.example.app_pasteleria_mil_sabores.ui.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.UsuarioRepository
import com.example.app_pasteleria_mil_sabores.model.Usuario
import kotlinx.coroutines.launch
import com.example.app_pasteleria_mil_sabores.data.SessionManager

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {
    val nombre = MutableLiveData("")
    val contrasena = MutableLiveData("")
    val loginExitoso = MutableLiveData<Usuario?>(null)
    val mensajeError = MutableLiveData<String?>(null)

    fun iniciarSesion() {
        val nombreValue = nombre.value.orEmpty()
        val contrasenaValue = contrasena.value.orEmpty()

        if (nombreValue.isBlank() || contrasenaValue.isBlank()) {
            mensajeError.value = "Ingrese su nombre y contraseña"
            return
        }

        viewModelScope.launch {
            val usuarioLogeado = repository.iniciarSesion(nombreValue, contrasenaValue)

            if (usuarioLogeado != null) {
                SessionManager.usuarioActual = usuarioLogeado
                loginExitoso.value = usuarioLogeado


                nombre.value = ""
                contrasena.value = ""
            } else {
                mensajeError.value = "Nombre de usuario o contraseña incorrecto"
            }
        }
    }


    fun limpiarMensajes() {
        mensajeError.value = null
    }
}