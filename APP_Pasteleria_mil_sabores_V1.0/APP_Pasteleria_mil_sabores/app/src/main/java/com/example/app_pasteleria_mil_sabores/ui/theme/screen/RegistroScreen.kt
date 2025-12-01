package com.example.app_pasteleria_mil_sabores.ui.theme.screen

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.FormularioViewModel


@Composable
fun RegistroScreen(viewModel: FormularioViewModel ){
    var nombre by remember { mutableStateListOf("") }
    var contrasena by remember { mutableStateListOf("") }

    val usuarios by viewModel.usuarios.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it},
            label = {Text(text = "Ingrese nombre")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            Value = contrasena,
            onValueChange = {contrasena = it},
            label = {Text(text = "Ingrese su contraseña")},
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if(nombre.isNotBlank()&& contrasena.isNotBlank()){
                    viewModel.agregarUsuarios(nombre, contrasena)
                nombre = ""
                contrasena=""
                }
            }, modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Agregar usuario")

        }

        Spacer(modifier = Modifier.height(8.dp))

    }

}