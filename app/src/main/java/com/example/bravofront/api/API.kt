package com.example.bravofront.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class API(private val ctx: Context?) {

    private val baseUrl = "https://vicentedev.com.br/"

    private val timeout = 30L

    private val retrofit: Retrofit
        get() {
            val okHttp = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build()

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

    private val authRetrofit : Retrofit
        get() {
            val authenticator = AuthenticateToken(ctx!!)

            val okHttp = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(authenticator)
                .authenticator(authenticator)
                .build()

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

    val home: HomeAPI
        get() = retrofit.create(HomeAPI::class.java)

    val produto: ProdutoAPI
        get() = retrofit.create(ProdutoAPI::class.java)

    val login: LoginAPI
        get() = retrofit.create(LoginAPI::class.java)

    val profile: ProfileAPI
        get() = authRetrofit.create(ProfileAPI::class.java)

    val cart: CartAPI
        get() = authRetrofit.create(CartAPI::class.java)
}