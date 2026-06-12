package com.example.myapplication.presentation.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.AuthSessionStorage
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.ProfileRepositoryImpl
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

    fun getProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val token = AuthSessionStorage.getToken()
            if (token.isNullOrBlank()) {
                _state.update { it.copy(isLoading = false, error = "No hay sesión activa.") }
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
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val database = AppDatabase.getInstance(application)
            val repository = ProfileRepositoryImpl(
                api = RetrofitClient.profileApi,
                dao = database.profileDao()
            )
            return ProfileViewModel(GetProfileUseCase(repository)) as T
        }
    }
}
