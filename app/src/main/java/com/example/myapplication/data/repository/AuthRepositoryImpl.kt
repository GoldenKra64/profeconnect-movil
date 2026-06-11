package com.example.myapplication.data.repository

import com.example.myapplication.data.local.AuthSessionStorage
import com.example.myapplication.data.remote.api.AuthApi
import com.example.myapplication.data.remote.dto.LoginRequestDto
import com.example.myapplication.domain.model.AuthSession
import com.example.myapplication.domain.model.AuthUser
import com.example.myapplication.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionStorage: AuthSessionStorage = AuthSessionStorage
) : AuthRepository {

    override suspend fun login(
        institutionalEmail: String,
        password: String
    ): Result<AuthSession> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.login(
                LoginRequestDto(
                    institutionalEmail = institutionalEmail,
                    password = password
                )
            )
            val data = response.data

            if (!response.success || data == null) {
                return@withContext Result.failure(Exception(response.message))
            }

            val session = AuthSession(
                token = data.token,
                user = AuthUser(
                    id = data.user.id,
                    institutionalEmail = data.user.institutionalEmail,
                    firstName = data.user.firstName,
                    lastName = data.user.lastName,
                    role = data.user.role,
                    status = data.user.status
                )
            )

            sessionStorage.saveSession(session)
            Result.success(session)
        } catch (e: HttpException) {
            Result.failure(Exception(extractApiErrorMessage(e)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractApiErrorMessage(error: HttpException): String {
        val rawBody = error.response()?.errorBody()?.string()
        if (!rawBody.isNullOrBlank()) {
            runCatching {
                val message = JSONObject(rawBody).optString("message")
                if (message.isNotBlank()) return message
            }
        }

        return "No se pudo iniciar sesion"
    }
}
