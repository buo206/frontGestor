package com.example.frontgestor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontgestor.Api.LoginViewModel
import com.example.frontgestor.Vistas.LoginScreen
import com.example.frontgestor.ui.theme.FrontGestorTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontGestorTheme {
                val session = SessionManager(this)
                Scaffold(modifier = Modifier.fillMaxSize().background(Color.White)) { innerPadding ->
                    val viewModel: LoginViewModel by viewModels()
                    if (session.isLogged()) {
                        Greeting("pepe" ,modifier = Modifier.padding(innerPadding))
                    } else {
                        LoginScreen(modifier = Modifier.padding(innerPadding) ,
                            viewModel ,
                            {

                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text("Hola $name")

    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Bienvenido al menú")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            session.logout()
        }) {
            Text("Cerrar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontGestorTheme {
        Greeting("Android")
    }
}