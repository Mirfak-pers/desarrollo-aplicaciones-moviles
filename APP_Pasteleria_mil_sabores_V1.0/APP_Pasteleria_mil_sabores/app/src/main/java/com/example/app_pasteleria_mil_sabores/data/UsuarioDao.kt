package com.example.app_pasteleria_mil_sabores.data

import androidx.room.Dao // NECESARIO: Para que Room reconozca la interfaz
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_pasteleria_mil_sabores.model.Usuario // Tu entidad está en 'model'

@Dao // NECESARIO: Anotar la interfaz como un Data Access Object
interface UsuarioDao {

    /**
     * Inserta un nuevo objeto Usuario en la base de datos.
     * Si hay conflicto (por ejemplo, con la clave primaria), reemplaza el registro.
     * @param usuario El objeto Usuario a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Usamos REPLACE para evitar errores si el ID existe
    suspend fun insertar(usuario: Usuario)

    /**
     * Obtiene todos los usuarios de la base de datos.
     * @return Una lista de objetos Usuario.
     */
    @Query("SELECT * FROM Usuario")
    suspend fun obtenerUsuarios() : List<Usuario>
}
