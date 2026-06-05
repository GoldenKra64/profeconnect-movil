package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientView(
    viewModel: ClientViewModel,
    onBack: () -> Unit
) {

    val client by viewModel.client.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onBack
            ) {
                Text("Volver")
            }

            Text(
                text = client?.name ?: "Sin cliente",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = client?.email ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = client?.name ?: "",
                onValueChange = {
                    viewModel.changeName(it)
                },
                label = {
                    Text("Nombre")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = client?.age?.toString() ?: "",
                onValueChange = {
                    viewModel.changeAge(it)
                },
                label = {
                    Text("Edad")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = client?.email ?: "",
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
                    viewModel.saveClient()
                }
            ) {
                Text("Guardar Cliente")
            }
        }
    }
}