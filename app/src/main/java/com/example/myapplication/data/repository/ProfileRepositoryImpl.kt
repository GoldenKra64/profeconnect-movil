package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ProfileDao
import com.example.myapplication.data.local.entity.ProfileEntity
import com.example.myapplication.data.remote.api.ProfileApi
import com.example.myapplication.domain.model.Profile
import com.example.myapplication.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val dao: ProfileDao
) : ProfileRepository {

    override suspend fun getProfile(token: String): Result<Profile> {
        return try {
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
                
                // Save to Room
                dao.insertProfile(
                    ProfileEntity(
                        id = profile.id,
                        institutionalEmail = profile.institutionalEmail,
                        firstName = profile.firstName,
                        lastName = profile.lastName,
                        role = profile.role,
                        profileImage = profile.profileImage
                    )
                )

                Result.success(profile)
            } else {
                Result.failure(Exception(response.message ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            // Fallback to Room
            val cachedProfile = dao.getProfile()
            if (cachedProfile != null) {
                Result.success(
                    Profile(
                        id = cachedProfile.id,
                        institutionalEmail = cachedProfile.institutionalEmail,
                        firstName = cachedProfile.firstName,
                        lastName = cachedProfile.lastName,
                        role = cachedProfile.role,
                        profileImage = cachedProfile.profileImage
                    )
                )
            } else {
                Result.failure(Exception("No hay registro en el dispositivo ni conexión a internet"))
            }
        }
    }
}
