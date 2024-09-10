package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {

    @POST("/api/login")
    fun login(@Body email: String, @Body password: String): Call<ApiResponse<Login>>
}