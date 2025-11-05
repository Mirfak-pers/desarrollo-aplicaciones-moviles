package com.example.app_pasteleria_mil_sabores.ui.profile


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_pasteleria_mil_sabores.data.SessionManager
import com.example.app_pasteleria_mil_sabores.model.Usuario

class PerfilViewModel : ViewModel() {

    val usuarioLogueado = MutableLiveData<Usuario?>(null)

    init {
        cargarPerfil()
    }

    fun cargarPerfil() {
        usuarioLogueado.value = SessionManager.usuarioActual
    }

    fun cerrarSesion() {
        SessionManager.usuarioActual = null
        usuarioLogueado.value = null
    }
}