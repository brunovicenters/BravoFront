package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {

    @POST("/api/login")
    fun login(@Body login: LoginRequest): Call<ApiResponse<Login>>
}