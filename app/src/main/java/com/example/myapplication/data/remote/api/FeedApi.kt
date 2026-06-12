package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.dto.ApiResponseDto
import com.example.myapplication.data.remote.dto.PaginatedPublicationsDto
import com.example.myapplication.data.remote.dto.PublicationDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface FeedApi {

    data class PublicationResponseDto(val items: List<PublicationDto>)
    // Obtiene el feed, soporta filtrado por etiqueta
    @GET("publications")
    suspend fun getFeed(@Query("tagId") tagId: Int? = null): ApiResponseDto<PublicationResponseDto>

    // Endpoint multipart para incluir arrays de archivos y datos de la publicación
    @Multipart
    @POST("publications")
    suspend fun createPublication(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("isAnonymous") isAnonymous: RequestBody,
        @Part("tags") tags: RequestBody, // JSON array stringificado
        @Part files: List<MultipartBody.Part>? // Corresponde a upload.array("files")
    ): ApiResponseDto<PublicationDto>
}