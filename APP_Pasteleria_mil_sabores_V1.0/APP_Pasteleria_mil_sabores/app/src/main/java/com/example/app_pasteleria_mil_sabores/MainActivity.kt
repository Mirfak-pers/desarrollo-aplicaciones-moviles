package com.example.app_pasteleria_mil_sabores

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_pasteleria_mil_sabores.data.SessionManager
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
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LaunchedEffect(Unit) {
                if (SessionManager.usuarioActual != null) {
                    navController.navigate(Routes.CATALOG) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                application = application,
                onIrARegistro = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginExitoso = { usuario ->
                    SessionManager.usuarioActual = usuario

                    navController.navigate(Routes.CATALOG) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegistroScreen(
                application = application,
                onRegistroExitoso = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CATALOG) {
            LaunchedEffect(Unit) {
                if (SessionManager.usuarioActual == null) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }

            CatalogoScreen(
                application = application,
                onIrAProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.PROFILE) {
            PerfilScreen(
                onCerrarSesion = {
                    SessionManager.usuarioActual = null

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onIrALogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}