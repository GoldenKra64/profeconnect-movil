package com.example.myapplication.data.remote.dto

data class ProfileDataDto(
    val id: Int,
    val institutionalEmail: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val profile: String?
)
