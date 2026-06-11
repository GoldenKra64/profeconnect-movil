package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.dto.ApiResponseDto
import com.example.myapplication.data.remote.dto.LoginRequestDto
import com.example.myapplication.data.remote.dto.LoginResponseDto
import com.example.myapplication.data.remote.dto.RegistrationRequestDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): ApiResponseDto<LoginResponseDto>

    @Multipart
    @POST("auth/register-request")
    suspend fun registerRequest(
        @Part("institutionalEmail") institutionalEmail: RequestBody,
        @Part("password") password: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("area") area: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part cedulaPhoto: MultipartBody.Part
    ): ApiResponseDto<RegistrationRequestDto>
}
