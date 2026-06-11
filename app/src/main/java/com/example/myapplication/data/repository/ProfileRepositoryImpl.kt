package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.api.ProfileApi
import com.example.myapplication.domain.model.Profile
import com.example.myapplication.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(token: String): Result<Profile> {
        return try {
            // Include 'Bearer ' if the backend expects it.
            val authHeader = if (token.isNotBlank() && !token.startsWith("Bearer ")) {
                "Bearer $token"
            } else {
                token
            }

            val response = api.getProfile(authHeader)

            if (response.success && response.data != null) {
                val profile = Profile(
                    id = response.data.id,
                    institutionalEmail = response.data.institutionalEmail,
                    firstName = response.data.firstName,
                    lastName = response.data.lastName,
                    role = response.data.role,
                    profileImage = response.data.profile
                )
                Result.success(profile)
            } else {
                Result.failure(Exception(response.message ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
