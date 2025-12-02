package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel

@Composable
fun EditProfileScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    var name by remember { mutableStateOf(user?.name ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            phone = it.phone ?: ""
        }
    }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is AuthViewModel.LoginState.Success -> {
                showError = false
                onNavigateBack()
            }
            is AuthViewModel.LoginState.Error -> {
                errorMessage = state.message
                showError = true
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Editar Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AnimatedVisibility(visible = showError) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.updateProfile(name, phone.ifBlank { null }) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}