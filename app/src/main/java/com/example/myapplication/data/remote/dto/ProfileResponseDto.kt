package com.example.myapplication.data.remote.dto

data class ProfileResponseDto(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: ProfileDataDto?
)
