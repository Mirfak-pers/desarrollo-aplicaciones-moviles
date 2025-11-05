package com.example.app_pasteleria_mil_sabores

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_pasteleria_mil_sabores.data.SessionManager // <-- Importar SessionManager
import com.example.app_pasteleria_mil_sabores.model.Usuario
import com.example.app_pasteleria_mil_sabores.ui.catalog.CatalogoScreen
import com.example.app_pasteleria_mil_sabores.ui.login.LoginScreen
import com.example.app_pasteleria_mil_sabores.ui.profile.PerfilScreen
import com.example.app_pasteleria_mil_sabores.ui.register.RegistroScreen
import com.example.app_pasteleria_mil_sabores.ui.theme.AppPasteleriaMilSaboresTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPasteleriaMilSaboresTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController, application = application)
            }
        }
    }
}

// Rutas simples como constantes
private object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CATALOG = "catalog"
    const val PROFILE = "profile"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    application: Application
) {
    // Determina la pantalla inicial usando el SessionManager
    val startDestination = if (SessionManager.usuarioActual != null) {
        Routes.CATALOG
    } else {
        Routes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- RUTA: REGISTRO ---
        composable(Routes.REGISTER) {
            RegistroScreen(
                application = application,
                onRegistroExitoso = { navController.navigate(Routes.LOGIN) }
            )
        }

        // --- RUTA: LOGIN ---
        composable(Routes.LOGIN) {
            LoginScreen(
                application = application,
                onIrARegistro = { navController.navigate(Routes.REGISTER) },
                onLoginExitoso = { _: Usuario ->
                    navController.navigate(Routes.CATALOG) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // --- RUTA: CATÁLOGO ---
        composable(Routes.CATALOG) {
            CatalogoScreen(
                application = application,
                onIrAProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        // --- RUTA: PERFIL ---
        composable(Routes.PROFILE) {
            PerfilScreen(
                // Cerrar sesión
                onCerrarSesion = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                // Ir a Login
                onIrALogin = { navController.navigate(Routes.LOGIN) }
            )
        }
    }
}