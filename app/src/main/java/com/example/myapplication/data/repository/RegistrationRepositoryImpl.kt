package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.RegistrationRequestDao
import com.example.myapplication.data.local.entity.RegistrationRequestEntity
import com.example.myapplication.data.remote.api.AuthApi
import com.example.myapplication.domain.model.DocenteRegistration
import com.example.myapplication.domain.model.RegistrationResult
import com.example.myapplication.domain.repository.RegistrationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RegistrationRepositoryImpl(
    private val authApi: AuthApi,
    private val dao: RegistrationRequestDao
) : RegistrationRepository {

    override suspend fun enviarSolicitud(
        docente: DocenteRegistration,
        password: String,
        cedulaPhoto: File
    ): RegistrationResult = withContext(Dispatchers.IO) {
        try {
            val emailBody = docente.institutionalEmail.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
            val firstNameBody = docente.firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = docente.lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val areaBody = docente.area?.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = docente.description?.toRequestBody("text/plain".toMediaTypeOrNull())
            val fotoPart = MultipartBody.Part.createFormData(
                name = "cedulaPhoto",
                filename = cedulaPhoto.name,
                body = cedulaPhoto.asRequestBody("image/*".toMediaTypeOrNull())
            )

            val response = authApi.registerRequest(
                institutionalEmail = emailBody,
                password = passwordBody,
                firstName = firstNameBody,
                lastName = lastNameBody,
                area = areaBody,
                description = descriptionBody,
                cedulaPhoto = fotoPart
            )

            if (response.success) {
                dao.insert(
                    RegistrationRequestEntity(
                        institutionalEmail = docente.institutionalEmail,
                        firstName = docente.firstName,
                        lastName = docente.lastName,
                        area = docente.area,
                        description = docente.description,
                        status = "PENDIENTE"
                    )
                )
                RegistrationResult.Success(response.message)
            } else {
                RegistrationResult.Error(response.message)
            }
        } catch (e: Exception) {
            RegistrationResult.Error(e.message ?: "Error desconocido al enviar la solicitud")
        }
    }

    override fun observarSolicitudesLocales(): Flow<List<DocenteRegistration>> =
        dao.observarTodas().map { entities ->
            entities.map { entity ->
                DocenteRegistration(
                    firstName = entity.firstName,
                    lastName = entity.lastName,
                    institutionalEmail = entity.institutionalEmail,
                    area = entity.area,
                    description = entity.description
                )
            }
        }
}
