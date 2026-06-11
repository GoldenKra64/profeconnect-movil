package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.dto.ProfileResponseDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {
    @GET("profiles/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponseDto
}
