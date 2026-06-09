package com.example.myapplication.data.remote.dto

data class ApiResponseDto<T>(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: T?
)
