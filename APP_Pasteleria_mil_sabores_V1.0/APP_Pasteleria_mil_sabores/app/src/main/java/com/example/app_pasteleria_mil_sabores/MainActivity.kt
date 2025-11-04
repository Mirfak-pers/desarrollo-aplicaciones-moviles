package com.example.app_pasteleria_mil_sabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.app_pasteleria_mil_sabores.data.UsuarioDatabase
import com.example.app_pasteleria_mil_sabores.ui.theme.APP_Pasteleria_mil_saboresTheme
import com.example.app_pasteleria_mil_sabores.viewmodel.FormularioViewModel
import com.example.app_pasteleria_mil_sabores.data.*
import com.example.app_pasteleria_mil_sabores.ui.theme.screen.RegistroScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APP_Pasteleria_mil_saboresTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
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
            UsuarioDatabase::class.java,
            "usuarios.db"
        ).build()
    }
    val viewModel = remember {
        FormularioViewModel(database.usuarioDao())
    }

    RegistroScreen(viewModel = viewModel)


}