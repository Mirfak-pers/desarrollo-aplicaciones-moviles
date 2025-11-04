

package com.example.app_pasteleria_mil_sabores.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.model.Usuario
import com.example.app_pasteleria_mil_sabores.data.UsuarioDao
import kotlinx.coroutines.launch

// El ViewModel necesita el DAO (Data Access Object) como dependencia para interactuar con la DB.
// Se inyecta a través del constructor.
class RegistroViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    /**
     * Guarda un nuevo usuario en la base de datos de forma asíncrona.
     * Es la función llamada desde RegistroScreen.kt.
     * @param nombre El nombre ingresado por el usuario.
     * @param contrasena La contraseña ingresada por el usuario.
     */
    fun registrarUsuario(nombre: String, contrasena: String) {
        // Ejecutamos la inserción en el viewModelScope para Coroutines,
        // asegurando que la operación de DB no bloquee la interfaz.
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = nombre,
                contrasena = contrasena
            )
            // Llama a la función de inserción definida en el DAO.
            usuarioDao.insert(nuevoUsuario)
        }
    }

    /**
     * Función que puedes añadir para verificar si un usuario ya existe.
     * Por ahora, solo tenemos la función de registro.
     */
    // fun verificarUsuario(nombre: String, contrasena: String) { ... }
}
