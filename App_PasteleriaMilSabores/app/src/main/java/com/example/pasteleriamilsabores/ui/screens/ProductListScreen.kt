package com.example.pasteleriamilsabores.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (Product) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onNavigateToAddProduct,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { productViewModel.loadProductsFromApi() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cargar API")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onNavigateToCart,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Carrito")
                }
            }

            // Mostrar nombre del usuario logueado
            user?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Bienvenido/a, ${it.name}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = {
                            user?.let { cartViewModel.addToCart(it.id, product.id) }
                        },
                        onEdit = { onNavigateToEditProduct(product) },
                        onDelete = { productViewModel.deleteProduct(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${String.format("%.2f", product.price)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            product.description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Carrito")
                }
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}