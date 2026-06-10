package com.example.myapplication.presentation.register

import android.net.Uri
import com.example.myapplication.domain.model.RegistrationResult

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val institutionalEmail: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val area: String = "",
    val description: String = "",
    val cedulaPhotoUri: Uri? = null,
    val cedulaPhotoFileName: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,
    val cedulaPhotoError: String? = null,
    val submissionResult: RegistrationResult = RegistrationResult.Idle
)
