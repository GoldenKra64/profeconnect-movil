package com.example.myapplication.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(
            institutionalEmail = savedStateHandle[KEY_EMAIL] ?: "",
            password = savedStateHandle[KEY_PASSWORD] ?: ""
        )
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        savedStateHandle[KEY_EMAIL] = value
        _uiState.update {
            it.copy(
                institutionalEmail = value,
                emailError = validateEmail(value),
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        savedStateHandle[KEY_PASSWORD] = value
        _uiState.update {
            it.copy(
                password = value,
                passwordError = if (value.isBlank()) "La contrasena es obligatoria" else null,
                errorMessage = null
            )
        }
    }

    fun login() {
        val state = _uiState.value
        val emailError = validateEmail(state.institutionalEmail)
        val passwordError = if (state.password.isBlank()) "La contrasena es obligatoria" else null

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    errorMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUseCase(
                institutionalEmail = state.institutionalEmail,
                password = state.password
            )

            result.onSuccess {
                savedStateHandle[KEY_PASSWORD] = ""
                _uiState.update {
                    it.copy(
                        password = "",
                        isLoading = false,
                        isLoggedIn = true,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        errorMessage = error.message ?: "No se pudo iniciar sesion"
                    )
                }
            }
        }
    }

    fun onLoginHandled() {
        _uiState.update { it.copy(isLoggedIn = false) }
    }

    private fun validateEmail(value: String): String? {
        return when {
            value.isBlank() -> "El correo institucional es obligatorio"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches() ->
                "Correo institucional invalido"
            else -> null
        }
    }

    class Factory(
        private val loginUseCase: LoginUseCase = LoginUseCase(
            AuthRepositoryImpl(RetrofitClient.authApi)
        )
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return LoginViewModel(
                savedStateHandle = extras.createSavedStateHandle(),
                loginUseCase = loginUseCase
            ) as T
        }
    }

    companion object {
        private const val KEY_EMAIL = "login_email"
        private const val KEY_PASSWORD = "login_password"
    }
}
