package com.example.pasteleriamilsabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pasteleriamilsabores.data.local.DatabaseHelper
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.remote.ApiService
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.ui.screens.*
import com.example.pasteleriamilsabores.ui.theme.PasteleriaMilSabores
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseHelper(this)
        val api = ApiService.create()
        val repository = Repository(db, api)

        val authViewModel = AuthViewModel(repository)
        val productViewModel = ProductViewModel(repository)
        val cartViewModel = CartViewModel(repository)

        setContent {
            PasteleriaMilSabores {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(authViewModel, productViewModel, cartViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel
) {
    val navController = rememberNavController()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    authViewModel.resetState()
                    navController.navigate("products") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    authViewModel.resetState()
                    navController.navigate("products") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("products") {
            ProductListScreen(
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel,
                onNavigateToAddProduct = { navController.navigate("add_product") },
                onNavigateToEditProduct = { product ->
                    selectedProduct = product
                    navController.navigate("edit_product")
                },
                onNavigateToCart = { navController.navigate("cart") },
                onNavigateToProfile = { navController.navigate("profile") } // NUEVO
            )
        }

        composable("add_product") {
            AddProductScreen(
                productViewModel = productViewModel,
                authViewModel = authViewModel,
                onNavigateBack = {
                    productViewModel.resetState()
                    navController.popBackStack()
                }
            )
        }

        composable("edit_product") {
            selectedProduct?.let { product ->
                EditProductScreen(
                    product = product,
                    productViewModel = productViewModel,
                    onNavigateBack = {
                        productViewModel.resetState()
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                viewModel = authViewModel,
                onNavigateToEdit = { navController.navigate("edit_profile") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    authViewModel.resetState()
                    navController.popBackStack()
                }
            )
        }
    }
}
