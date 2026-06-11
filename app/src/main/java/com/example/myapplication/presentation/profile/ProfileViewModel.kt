package com.example.myapplication.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    // Este token temporalmente está en blanco o puede ser reemplazado
    fun getProfile(token: String = "") {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // Simulamos datos de prueba mientras no tengamos un token real
            if (token.isBlank()) {
                val mockProfile = com.example.myapplication.domain.model.Profile(
                    id = 1,
                    institutionalEmail = "admin@institucion.edu.ec",
                    firstName = "Administrador",
                    lastName = "Sistema",
                    role = "admin",
                    profileImage = null
                )
                _state.update { it.copy(isLoading = false, profile = mockProfile) }
                return@launch
            }

            val result = getProfileUseCase(token)
            
            result.onSuccess { profile ->
                _state.update { it.copy(isLoading = false, profile = profile) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    class Factory(
        private val getProfileUseCase: GetProfileUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(getProfileUseCase) as T
        }
    }
}
