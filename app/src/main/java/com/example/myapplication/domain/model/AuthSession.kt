package com.example.myapplication.domain.model

data class AuthSession(
    val token: String,
    val user: AuthUser
)

data class AuthUser(
    val id: Int,
    val institutionalEmail: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val status: String
)
