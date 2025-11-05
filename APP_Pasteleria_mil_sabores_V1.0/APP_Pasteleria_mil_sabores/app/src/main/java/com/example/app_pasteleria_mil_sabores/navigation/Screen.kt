package com.example.app_pasteleria_mil_sabores.navigation

/**
 * Define las rutas de navegación (las "direcciones" de tus pantallas).
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Catalog : Screen("catalog")
    object Profile : Screen("profile")
}