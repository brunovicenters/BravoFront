package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Home
import retrofit2.Call
import retrofit2.http.GET

interface HomeAPI {
    @GET("/api")
    fun index(): Call<ApiResponse<Home>>
}