package com.example.app_pasteleria_mil_sabores.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf // Necesitas esta importación para mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue // Necesitas esta importación para la sintaxis 'by'
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_pasteleria_mil_sabores.ui.viewmodel.RegistroViewModel // Importa el ViewModel correcto

@Composable
fun RegistroScreen(viewModel: RegistroViewModel){ // Usa el ViewModel para la lógica

    // CORRECCIÓN: Usamos mutableStateOf para el estado de los campos de texto
    // Esto resuelve el error de 'Property delegate must have getValue/setValue'
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {Text(text = "Ingrese nombre")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasena, // 'value' en minúscula
            onValueChange = { contrasena = it },
            label = {Text(text = "Ingrese su contraseña")},
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Verificamos que ambos campos tengan texto
                if(nombre.isNotBlank() && contrasena.isNotBlank()){
                    // Llama al ViewModel para guardar los datos
                    viewModel.registrarUsuario(nombre, contrasena)

                    // Limpiamos los campos después del registro
                    nombre = ""
                    contrasena = ""
                }
            }, modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Agregar usuario")
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}
