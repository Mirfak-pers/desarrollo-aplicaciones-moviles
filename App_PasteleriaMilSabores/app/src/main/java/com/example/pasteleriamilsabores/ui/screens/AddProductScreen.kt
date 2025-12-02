package com.example.pasteleriamilsabores.ui.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel

@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val productState by productViewModel.productState.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

    LaunchedEffect(productState) {
        when (val state = productState) {
            is ProductViewModel.ProductState.Success -> {
                showError = false
                onNavigateBack()
            }
            is ProductViewModel.ProductState.Error -> {
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
            text = "Agregar Producto",
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
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = image,
            onValueChange = { image = it },
            label = { Text("URL de Imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                user?.let {
                    productViewModel.addProduct(title, price, description, image, it.id)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
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