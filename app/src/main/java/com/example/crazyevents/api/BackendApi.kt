package com.example.crazyevents.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object BackendApi {
    private const val BASE_URL = "http://10.0.2.2:3000" // Use 10.0.2.2 for emulator localhost
    // OR use "http://192.168.0.178:3000/" if accessing from a physical device or a different network configuration

    val api: BackendApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }
}