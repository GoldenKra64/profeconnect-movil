package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.business.esMayorDeEdad
import com.example.myapplication.business.procesarAsistente
import com.example.myapplication.business.registrarAsistente
import com.example.myapplication.data.Asistente
import com.example.myapplication.data.RegistroState
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SafePassScreen()
            }
        }
    }
}

@Composable
fun SafePassScreen() {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var tipoEntrada by remember { mutableStateOf("") }
    var state by remember { mutableStateOf<RegistroState>(RegistroState.Idle) }

    Scaffold { padding ->

        Column(modifier = Modifier.padding(padding)) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )

            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text("Edad") }
            )

            OutlinedTextField(
                value = tipoEntrada,
                onValueChange = { tipoEntrada = it },
                label = { Text("Tipo de entrada") }
            )

            Button(onClick = {
                state = registrarAsistente(nombre, edad, tipoEntrada)
            }) {
                Text("Registrar")
            }

            when (state) {

                is RegistroState.Idle -> {
                    Text("Ingrese los datos del asistente")
                }

                is RegistroState.Success -> {
                    val success = state as RegistroState.Success
                    Text(success.mensaje)
                }

                is RegistroState.Error -> {
                    val error = state as RegistroState.Error
                    Text("Error: ${error.message}")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Bienvenido al curso 2026, $name",
        color = Color.Blue,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}