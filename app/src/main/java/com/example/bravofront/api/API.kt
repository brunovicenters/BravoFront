package com.example.bravofront.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {

    private const val BASE_URL = "http://10.135.151.17:8000/"

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

    val home: HomeAPI
        get() = retrofit.create(HomeAPI::class.java)

    val search: SearchAPI
        get() = retrofit.create(SearchAPI::class.java)

    val login: LoginAPI
        get() = retrofit.create(LoginAPI::class.java)
}