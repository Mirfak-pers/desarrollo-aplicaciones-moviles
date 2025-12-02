package com.example.pasteleriamilsabores.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onNavigateToEdit: () -> Unit,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        user?.let { currentUser ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nombre: ${currentUser.name}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: ${currentUser.email}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teléfono: ${currentUser.phone ?: "No especificado"}",
                        style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(
                onClick = onNavigateToEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}