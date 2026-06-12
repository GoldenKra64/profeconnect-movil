package com.example.myapplication.data.remote

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.local.AuthSessionStorage
import com.example.myapplication.data.remote.api.AuthApi
import com.example.myapplication.data.remote.api.FeedApi
import com.example.myapplication.data.remote.api.ProfileApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request()
        val encodedPath = request.url.encodedPath
        val isPublicAuthEndpoint = encodedPath.endsWith("/auth/login") ||
            encodedPath.endsWith("/auth/register-request")
        val token = AuthSessionStorage.getToken()

        val authenticatedRequest = if (!isPublicAuthEndpoint && token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        chain.proceed(authenticatedRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private fun resolvedBaseUrl(): String {
        return "https://profeconnect-backend.up.railway.app/api/v1/"
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(resolvedBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val profileApi: ProfileApi by lazy { retrofit.create(ProfileApi::class.java) }
    val feedApi: FeedApi by lazy { retrofit.create(FeedApi::class.java) }
}

