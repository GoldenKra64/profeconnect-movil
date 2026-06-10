package com.example.myapplication.data.remote

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.remote.api.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val DEFAULT_BASE_URL = "https://profeconnect-backend.up.railway.app/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private fun resolvedBaseUrl(): String {
        val raw = BuildConfig.API_URL.trim().removeSurrounding("\"")
        val base = raw.ifBlank { DEFAULT_BASE_URL.removeSuffix("/") }
        return if (base.endsWith("/")) base else "$base/"
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(resolvedBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
}
