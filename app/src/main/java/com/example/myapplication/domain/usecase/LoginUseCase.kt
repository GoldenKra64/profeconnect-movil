package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.AuthSession
import com.example.myapplication.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        institutionalEmail: String,
        password: String
    ): Result<AuthSession> {
        if (institutionalEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo institucional es obligatorio"))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(institutionalEmail.trim()).matches()) {
            return Result.failure(IllegalArgumentException("Correo institucional invalido"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contrasena es obligatoria"))
        }

        return repository.login(
            institutionalEmail = institutionalEmail.trim(),
            password = password
        )
    }
}
