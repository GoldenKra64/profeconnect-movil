package com.example.myapplication.presentation.register

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.RegistrationRepositoryImpl
import com.example.myapplication.domain.model.DocenteRegistration
import com.example.myapplication.domain.model.RegistrationResult
import com.example.myapplication.domain.usecase.RegisterDocenteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val useCase = RegisterDocenteUseCase(
        RegistrationRepositoryImpl(RetrofitClient.authApi)
    )

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFirstNameChange(value: String) = _uiState.update {
        it.copy(firstName = value, firstNameError = if (value.isBlank()) "El nombre es obligatorio" else null)
    }

    fun onLastNameChange(value: String) = _uiState.update {
        it.copy(lastName = value, lastNameError = if (value.isBlank()) "El apellido es obligatorio" else null)
    }

    fun onEmailChange(value: String) = _uiState.update {
        it.copy(
            institutionalEmail = value,
            emailError = if (value.isNotBlank() && !isValidEmail(value)) "Correo institucional inválido" else null
        )
    }

    fun onPasswordChange(value: String) = _uiState.update {
        it.copy(
            password = value,
            passwordError = if (value.isNotBlank() && value.length < 8) "Mínimo 8 caracteres" else null,
            passwordConfirmError = if (it.passwordConfirm.isNotBlank() && it.passwordConfirm != value)
                "Las contraseñas no coinciden" else null
        )
    }

    fun onPasswordConfirmChange(value: String) = _uiState.update {
        it.copy(
            passwordConfirm = value,
            passwordConfirmError = if (value.isNotBlank() && value != it.password) "Las contraseñas no coinciden" else null
        )
    }

    fun onAreaChange(value: String) = _uiState.update { it.copy(area = value) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    fun onCedulaPhotoSelected(uri: Uri?, fileName: String?) = _uiState.update {
        it.copy(
            cedulaPhotoUri = uri,
            cedulaPhotoFileName = fileName,
            cedulaPhotoError = if (uri == null) "Debe adjuntar una foto de cédula" else null
        )
    }

    fun resetResult() = _uiState.update { it.copy(submissionResult = RegistrationResult.Idle) }

    fun enviarSolicitud() {
        val state = _uiState.value
        if (!validate(state)) return
        val context = getApplication<Application>()

        viewModelScope.launch {
            _uiState.update { it.copy(submissionResult = RegistrationResult.Loading) }

            val tempFile = uriToTempFile(state.cedulaPhotoUri!!)
            if (tempFile == null) {
                _uiState.update {
                    it.copy(submissionResult = RegistrationResult.Error("No se pudo leer el archivo de cédula"))
                }
                return@launch
            }

            val result = useCase(
                docente = DocenteRegistration(
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    institutionalEmail = state.institutionalEmail.trim(),
                    area = state.area.trim().ifBlank { null },
                    description = state.description.trim().ifBlank { null }
                ),
                password = state.password,
                cedulaPhoto = tempFile
            )

            _uiState.update { it.copy(submissionResult = result) }
            tempFile.delete()
        }
    }

    private fun validate(state: RegisterUiState): Boolean {
        var valid = true
        _uiState.update { s ->
            s.copy(
                firstNameError = if (state.firstName.isBlank()) { valid = false; "El nombre es obligatorio" } else null,
                lastNameError = if (state.lastName.isBlank()) { valid = false; "El apellido es obligatorio" } else null,
                emailError = when {
                    state.institutionalEmail.isBlank() -> { valid = false; "El correo es obligatorio" }
                    !isValidEmail(state.institutionalEmail) -> { valid = false; "Correo institucional inválido" }
                    else -> null
                },
                passwordError = when {
                    state.password.isBlank() -> { valid = false; "La contraseña es obligatoria" }
                    state.password.length < 8 -> { valid = false; "Mínimo 8 caracteres" }
                    else -> null
                },
                passwordConfirmError = when {
                    state.passwordConfirm.isBlank() -> { valid = false; "Confirma tu contraseña" }
                    state.password != state.passwordConfirm -> { valid = false; "Las contraseñas no coinciden" }
                    else -> null
                },
                cedulaPhotoError = if (state.cedulaPhotoUri == null) { valid = false; "Debe adjuntar una foto de cédula" } else null
            )
        }
        return valid
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    private fun uriToTempFile(uri: Uri): File? {
        return try {
            val context = getApplication<Application>()
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("cedula_", ".tmp", context.cacheDir)
            tempFile.outputStream().use { inputStream.copyTo(it) }
            tempFile
        } catch (e: Exception) {
            null
        }
    }
}
