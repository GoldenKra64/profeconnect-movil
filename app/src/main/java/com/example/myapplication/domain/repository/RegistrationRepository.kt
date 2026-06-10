package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.DocenteRegistration
import com.example.myapplication.domain.model.RegistrationResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface RegistrationRepository {
    suspend fun enviarSolicitud(
        docente: DocenteRegistration,
        password: String,
        cedulaPhoto: File
    ): RegistrationResult

    fun observarSolicitudesLocales(): Flow<List<DocenteRegistration>>
}
