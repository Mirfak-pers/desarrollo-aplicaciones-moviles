package com.example.pasteleriamilsabores.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilsabores.data.model.CartItem
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

    LaunchedEffect(user) {
        user?.let { cartViewModel.loadCart(it.id) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Carrito de Compras",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cartItems) { item ->
                CartItemCard(
                    item = item,
                    onRemove = {
                        user?.let { cartViewModel.removeFromCart(item.id, it.id) }
                    }
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Total: $${String.format("%.2f", cartViewModel.getTotalPrice())}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Continuar Comprando")
                    }
                    Button(
                        onClick = { /* Checkout logic */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pagar")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.product.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Cantidad: ${item.quantity}")
                Text(text = "$${item.product.price * item.quantity}")
            }
            OutlinedButton(onClick = onRemove) {
                Text("Eliminar")
            }
        }
    }
}