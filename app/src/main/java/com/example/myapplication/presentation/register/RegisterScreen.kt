package com.example.myapplication.presentation.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.domain.model.RegistrationResult
import com.example.myapplication.ui.theme.MyApplicationTheme

// ── Stateful ──────────────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        val fileName = uri?.let { u ->
            context.contentResolver.query(u, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) cursor.getString(nameIndex) else null
            }
        }
        viewModel.onCedulaPhotoSelected(uri, fileName)
    }

    RegisterContent(
        uiState = uiState,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordConfirmChange = viewModel::onPasswordConfirmChange,
        onAreaChange = viewModel::onAreaChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onPickFile = { fileLauncher.launch(arrayOf("image/*", "application/pdf")) },
        onSubmit = { viewModel.enviarSolicitud() },
        onDismissResult = viewModel::resetResult,
        onNavigateBack = onNavigateBack
    )
}

// ── Stateless ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onAreaChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPickFile: () -> Unit,
    onSubmit: () -> Unit,
    onDismissResult: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val isLoading = uiState.submissionResult is RegistrationResult.Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitud de Registro") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Completa tus datos. Un administrador revisará y aprobará tu acceso.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            // Nombre y Apellido
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RegisterField(
                    modifier = Modifier.weight(1f),
                    label = "Nombres *",
                    value = uiState.firstName,
                    onValueChange = onFirstNameChange,
                    errorMessage = uiState.firstNameError,
                    enabled = !isLoading
                )
                RegisterField(
                    modifier = Modifier.weight(1f),
                    label = "Apellidos *",
                    value = uiState.lastName,
                    onValueChange = onLastNameChange,
                    errorMessage = uiState.lastNameError,
                    enabled = !isLoading
                )
            }

            RegisterField(
                label = "Correo institucional *",
                value = uiState.institutionalEmail,
                onValueChange = onEmailChange,
                errorMessage = uiState.emailError,
                keyboardType = KeyboardType.Email,
                placeholder = "docente@institucion.edu.ec",
                enabled = !isLoading
            )

            RegisterField(
                label = "Área pedagógica",
                value = uiState.area,
                onValueChange = onAreaChange,
                placeholder = "Matemáticas, Lengua, Ciencias...",
                enabled = !isLoading
            )

            PasswordField(
                label = "Contraseña *",
                value = uiState.password,
                onValueChange = onPasswordChange,
                errorMessage = uiState.passwordError,
                supportingText = "Mínimo 8 caracteres",
                enabled = !isLoading
            )

            PasswordField(
                label = "Confirmar contraseña *",
                value = uiState.passwordConfirm,
                onValueChange = onPasswordConfirmChange,
                errorMessage = uiState.passwordConfirmError,
                enabled = !isLoading
            )

            // Foto cédula
            Column {
                Text(
                    text = "Foto de cédula *",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                OutlinedButton(
                    onClick = onPickFile,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(uiState.cedulaPhotoFileName ?: "Seleccionar archivo")
                }
                if (uiState.cedulaPhotoError != null) {
                    Text(
                        text = uiState.cedulaPhotoError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (uiState.cedulaPhotoUri != null && uiState.cedulaPhotoError == null) {
                    Text(
                        text = "Archivo seleccionado",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            RegisterField(
                label = "Descripción / experiencia (opcional)",
                value = uiState.description,
                onValueChange = onDescriptionChange,
                placeholder = "Cuéntanos brevemente sobre tu trayectoria docente",
                minLines = 3,
                maxLines = 5,
                enabled = !isLoading
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onSubmit,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar solicitud")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    // Dialogo de resultado
    when (val result = uiState.submissionResult) {
        is RegistrationResult.Success -> {
            AlertDialog(
                onDismissRequest = onDismissResult,
                title = { Text("Solicitud enviada") },
                text = { Text(result.message) },
                confirmButton = {
                    TextButton(onClick = onDismissResult) { Text("Aceptar") }
                }
            )
        }
        is RegistrationResult.Error -> {
            AlertDialog(
                onDismissRequest = onDismissResult,
                title = { Text("Error") },
                text = { Text(result.message) },
                confirmButton = {
                    TextButton(onClick = onDismissResult) { Text("Cerrar") }
                }
            )
        }
        else -> Unit
    }
}

// ── Componentes auxiliares ─────────────────────────────────────────────────────

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1,
    enabled: Boolean = true,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        isError = errorMessage != null,
        supportingText = when {
            errorMessage != null -> { { Text(errorMessage, color = MaterialTheme.colorScheme.error) } }
            supportingText != null -> { { Text(supportingText) } }
            else -> null
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        minLines = minLines,
        maxLines = maxLines,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = when {
            errorMessage != null -> { { Text(errorMessage, color = MaterialTheme.colorScheme.error) } }
            supportingText != null -> { { Text(supportingText) } }
            else -> null
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    )
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterContentPreview() {
    MyApplicationTheme {
        RegisterContent(
            uiState = RegisterUiState(
                firstName = "Angel",
                lastName = "Fonseca",
                institutionalEmail = "angel@uni.edu.ec"
            ),
            onFirstNameChange = {},
            onLastNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordConfirmChange = {},
            onAreaChange = {},
            onDescriptionChange = {},
            onPickFile = {},
            onSubmit = {},
            onDismissResult = {},
            onNavigateBack = {}
        )
    }
}
