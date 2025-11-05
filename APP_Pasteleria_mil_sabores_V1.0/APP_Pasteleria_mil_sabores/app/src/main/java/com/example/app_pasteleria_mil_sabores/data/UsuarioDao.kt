package com.example.app_pasteleria_mil_sabores.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_pasteleria_mil_sabores.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE nombre = :nombre")
    suspend fun obtenerPorNombre(nombre: String): Usuario?

    @Query("SELECT * FROM usuario WHERE nombre = :nombre AND contrasena = :contrasena")
    suspend fun login(nombre: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuario")
    suspend fun obtenerUsuarios() : List<Usuario>
}