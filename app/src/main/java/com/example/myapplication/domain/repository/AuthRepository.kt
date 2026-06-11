package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.AuthSession

interface AuthRepository {
    suspend fun login(
        institutionalEmail: String,
        password: String
    ): Result<AuthSession>
}
