package com.example.app_pasteleria_mil_sabores.ui.login

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_pasteleria_mil_sabores.data.SessionManager
import com.example.app_pasteleria_mil_sabores.model.Usuario
import com.example.app_pasteleria_mil_sabores.viewmodel.AppViewModelFactory
import com.example.app_pasteleria_mil_sabores.ui.login.LoginViewModel

@Composable
fun LoginScreen(
    application: Application,
    onLoginExitoso: (Usuario) -> Unit,
    onIrARegistro: () -> Unit
) {
    val factory = AppViewModelFactory(application)
    val viewModel: LoginViewModel = viewModel(factory = factory)

    val nombre by viewModel.nombre.observeAsState("")
    val contrasena by viewModel.contrasena.observeAsState("")
    val loginExitoso by viewModel.loginExitoso.observeAsState()
    val mensajeError by viewModel.mensajeError.observeAsState()

    val context = LocalContext.current

    LaunchedEffect(loginExitoso) {
        loginExitoso?.let { usuario ->
            SessionManager.usuarioActual = usuario
            onLoginExitoso(usuario)
        }
    }

    LaunchedEffect(mensajeError) {
        mensajeError?.let { mensaje ->
            if (mensaje.isNotEmpty()) {
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { viewModel.nombre.value = it },
            label = { Text("Nombre de Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { viewModel.contrasena.value = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (!mensajeError.isNullOrEmpty()) {
            Text(
                text = mensajeError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = viewModel::iniciarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onIrARegistro) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}