package com.example.myapplication.data.remote.dto

data class AuthUserDto(
    val id: Int,
    val institutionalEmail: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val status: String
)
