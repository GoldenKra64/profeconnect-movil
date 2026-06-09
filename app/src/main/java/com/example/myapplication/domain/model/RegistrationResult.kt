package com.example.myapplication.domain.model

sealed interface RegistrationResult {
    data object Idle : RegistrationResult
    data object Loading : RegistrationResult
    data class Success(val message: String) : RegistrationResult
    data class Error(val message: String) : RegistrationResult
}
