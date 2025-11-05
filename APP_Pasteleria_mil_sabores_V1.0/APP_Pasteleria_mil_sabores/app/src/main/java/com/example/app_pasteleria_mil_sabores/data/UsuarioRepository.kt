package com.example.app_pasteleria_mil_sabores.data

import com.example.app_pasteleria_mil_sabores.model.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {


    suspend fun registrarUsuario(usuario: Usuario) {
        usuarioDao.insertar(usuario)
    }


    suspend fun obtenerUsuarioPorNombre(nombre: String): Usuario? {
        return usuarioDao.obtenerPorNombre(nombre)
    }


    suspend fun login(nombre: String, contrasena: String): Usuario? {
        return usuarioDao.login(nombre, contrasena)
    }

    suspend fun iniciarSesion(nombre: String, contrasena: String): Usuario? {
        return usuarioDao.login(nombre, contrasena)
    }
}