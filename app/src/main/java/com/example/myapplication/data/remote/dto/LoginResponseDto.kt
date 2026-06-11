package com.example.myapplication.data.remote.dto

data class LoginResponseDto(
    val token: String,
    val user: AuthUserDto
)
