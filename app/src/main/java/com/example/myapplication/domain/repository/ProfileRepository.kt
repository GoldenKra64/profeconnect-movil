package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(token: String): Result<Profile>
}
