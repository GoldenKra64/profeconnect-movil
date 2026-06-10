package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegistrationRequestDto(
    val id: Int? = null,
    @SerializedName("institutionalEmail") val institutionalEmail: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    val area: String? = null,
    val description: String? = null,
    val status: String? = null,
    val createdAt: String? = null
)
