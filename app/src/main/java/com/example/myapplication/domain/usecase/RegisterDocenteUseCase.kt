package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.DocenteRegistration
import com.example.myapplication.domain.model.RegistrationResult
import com.example.myapplication.domain.repository.RegistrationRepository
import java.io.File

class RegisterDocenteUseCase(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(
        docente: DocenteRegistration,
        password: String,
        cedulaPhoto: File
    ): RegistrationResult {
        if (docente.firstName.isBlank()) return RegistrationResult.Error("El nombre es obligatorio")
        if (docente.lastName.isBlank()) return RegistrationResult.Error("El apellido es obligatorio")
        if (!docente.institutionalEmail.contains('@')) return RegistrationResult.Error("Correo institucional inválido")
        if (password.length < 8) return RegistrationResult.Error("La contraseña debe tener al menos 8 caracteres")
        if (!cedulaPhoto.exists()) return RegistrationResult.Error("Debe adjuntar una foto de cédula")

        return repository.enviarSolicitud(docente, password, cedulaPhoto)
    }
}
