package com.example.app_pasteleria_mil_sabores.ui.register

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.AppViewModelFactory

@Composable
fun RegistroScreen(

    application: Application,
    onRegistroExitoso: () -> Unit
) {
    val factory = AppViewModelFactory(application)
    val viewModel: RegistroViewModel = viewModel(factory = factory)

    val nombre by viewModel.nombre.observeAsState("")
    val contrasena by viewModel.contrasena.observeAsState("")

    val registroExitoso by viewModel.registroExitoso.observeAsState(initial = false)
    val mensajeError by viewModel.mensajeError.observeAsState()

    val context = LocalContext.current


    LaunchedEffect(mensajeError) {
        if (mensajeError != null) {
            Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
            viewModel.limpiarMensajes()
        }
    }

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            Toast.makeText(context, "Registro exitoso. Inicie sesión.", Toast.LENGTH_LONG).show()
            onRegistroExitoso()
            viewModel.limpiarEstadoExito()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { viewModel.nombre.value = it },
            label = {Text(text = "Ingrese nombre de usuario")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { viewModel.contrasena.value = it },
            label = {Text(text = "Ingrese su contraseña")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.registrarUsuario() },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Registrar Usuario")
        }
    }
}