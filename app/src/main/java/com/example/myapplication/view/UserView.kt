package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun UserView(
    viewModel: UserViewModel,
    onNavigateToClient: () -> Unit
) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = user?.name ?: "Sin usuario",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = user?.email ?: ""
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveUser()
            }
        ) {
            Text("Cargar Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user?.name ?: "",
            onValueChange = {
                viewModel.changeName(it)
            },
            label = {
                Text("Nombre")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = user?.email ?: "",
            onValueChange = {
                viewModel.changeEmail(it)
            },
            label = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveUser()
                }
        ) {
            Text("Guardar Usuario")
        }

        Button(
            onClick = onNavigateToClient
        ) {
            Text("Ir a Clientes")
        }
    }
}