package com.example.myapplication.domain.model

data class Profile(
    val id: Int,
    val institutionalEmail: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val profileImage: String?
)
