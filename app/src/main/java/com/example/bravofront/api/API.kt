package com.example.bravofront.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {

    // CIT: 10.135.151.13
    // Sala PMD: 10.135.200.85
    // Casa: 192.168.15.13

    private const val BASE_URL = "http://192.168.15.13:8000/api/"

    private const val TIMEOUT = 30L

    private val retrofit: Retrofit
        get() {
            val okHttp = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()

            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

    val home: HomeApi
        get() = retrofit.create(HomeApi::class.java)

    val search: SearchAPI
        get() = retrofit.create(SearchAPI::class.java)
}