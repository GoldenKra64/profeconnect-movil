package com.example.myapplication.presentation.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
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

class RegisterViewModel(context: Context) : ViewModel() {

    private val dao = AppDatabase.getInstance(context).registrationRequestDao()
    private val useCase = RegisterDocenteUseCase(
        RegistrationRepositoryImpl(RetrofitClient.authApi, dao)
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
            emailError = if (value.isNotBlank() && !value.contains('@')) "Correo institucional inválido" else null
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
            passwordConfirmError = if (value != it.password) "Las contraseñas no coinciden" else null
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

    fun enviarSolicitud(context: Context) {
        val state = _uiState.value
        if (!validate(state)) return

        viewModelScope.launch {
            _uiState.update { it.copy(submissionResult = RegistrationResult.Loading) }

            val tempFile = uriToTempFile(context, state.cedulaPhotoUri!!)
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
                emailError = if (!state.institutionalEmail.contains('@')) { valid = false; "Correo institucional inválido" } else null,
                passwordError = if (state.password.length < 8) { valid = false; "Mínimo 8 caracteres" } else null,
                passwordConfirmError = if (state.password != state.passwordConfirm) { valid = false; "Las contraseñas no coinciden" } else null,
                cedulaPhotoError = if (state.cedulaPhotoUri == null) { valid = false; "Debe adjuntar una foto de cédula" } else null
            )
        }
        return valid
    }

    private fun uriToTempFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("cedula_", ".tmp", context.cacheDir)
            tempFile.outputStream().use { inputStream.copyTo(it) }
            tempFile
        } catch (e: Exception) {
            null
        }
    }
}
