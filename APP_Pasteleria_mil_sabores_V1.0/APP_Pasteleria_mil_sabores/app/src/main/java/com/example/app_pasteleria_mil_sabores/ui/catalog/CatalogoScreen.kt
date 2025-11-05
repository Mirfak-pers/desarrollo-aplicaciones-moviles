package com.example.app_pasteleria_mil_sabores.ui.catalog

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.AppViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import android.app.Application
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import com.example.app_pasteleria_mil_sabores.model.Producto
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    application: Application,
    onIrAProfile: () -> Unit
) {
    val factory = AppViewModelFactory(application)
    val viewModel: CatalogoViewModel = viewModel(factory = factory)

    val productos by viewModel.listaProductos.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Mil Sabores") },
                actions = {
                    IconButton(onClick = onIrAProfile) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Ir a Perfil"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos) { producto ->
                    ListItem(
                        // Muestra el nombre del producto
                        headlineContent = { Text(producto.nombre) },
                        // Muestra la categoría y el precio
                        supportingContent = {
                            Column {
                                Text("Categoría: ${producto.categoria}")
                                Text("Código: ${producto.codigo}")
                            }
                        },
                        trailingContent = {
                            // Mostrar precio en formato CLP (pesos chilenos)
                            Text("$${producto.precio} CLP", style = MaterialTheme.typography.titleMedium)
                        }
                    )
                    Divider()
                }
            }
        }
    }
}