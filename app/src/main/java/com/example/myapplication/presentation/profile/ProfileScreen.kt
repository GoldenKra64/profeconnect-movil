package com.example.myapplication.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        // Llama al getProfile al iniciar la pantalla (con token vacío por ahora)
        viewModel.getProfile("") 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.error != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error de la API:",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getProfile("") }) {
                            Text("Reintentar")
                        }
                    }
                }
                state.profile != null -> {
                    val profile = state.profile!!
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            // Al no tener Coil todavía, usamos un ícono por defecto
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Nombre
                        Text(
                            text = "${profile.firstName} ${profile.lastName}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Rol
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = profile.role.uppercase(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Detalles (Correo)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Correo Institucional",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = profile.institutionalEmail,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
