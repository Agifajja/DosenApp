package com.example.doosen.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.tif.uin-suska.ac.id/setoran-dev/v1/"
    private const val AUTH_BASE_URL = "https://id.tif.uin-suska.ac.id/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiServiceRetrofit by lazy {
        retrofit.create(ApiServiceRetrofit::class.java)
    }

    // Untuk login ke Keycloak
    val authService: ApiServiceRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceRetrofit::class.java)
    }
}
