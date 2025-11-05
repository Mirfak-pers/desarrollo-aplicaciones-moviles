package com.example.app_pasteleria_mil_sabores

import androidx.compose.runtime.remember
import androidx.room.Room


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_pasteleria_mil_sabores.data.AppDatabase
import com.example.app_pasteleria_mil_sabores.ui.theme.APP_Pasteleria_mil_saboresTheme
import com.example.app_pasteleria_mil_sabores.ui.register.RegistroScreen
import com.example.app_pasteleria_mil_sabores.ui.register.RegistroViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.app_pasteleria_mil_sabores.viewmodel.FormularioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicializar la Base de Datos
        // NOTA: Usamos applicationContext, que es el contexto de toda la aplicación.
        val database = AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()

        setContent {
            APP_Pasteleria_mil_saboresTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 3. Obtener el ViewModel y pasarlo a la pantalla
                    val registroViewModel: RegistroViewModel = viewModel(factory = factory)

                    // Aquí llamamos a la pantalla principal
                    RegistroScreen(
                        viewModel = registroViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    APP_Pasteleria_mil_saboresTheme {
        Greeting("Android")
    }
}

@Composable
fun FormularioApp(){
    val context = LocalContext.current

    val database = remember{
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "usuarios.db"
        ).build()
    }
    val viewModel = remember {
        FormularioViewModel(database.usuarioDao())
    }

    RegistroScreen(viewModel = viewModel)


}