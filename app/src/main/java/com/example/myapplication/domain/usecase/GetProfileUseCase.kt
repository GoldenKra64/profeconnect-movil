package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Profile
import com.example.myapplication.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(token: String): Result<Profile> {
        return repository.getProfile(token)
    }
}
