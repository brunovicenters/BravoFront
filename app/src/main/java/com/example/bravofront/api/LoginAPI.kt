package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.LoginRequest
import com.example.bravofront.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {

    @POST("/api/login")
    fun login(@Body login: LoginRequest): Call<ApiResponse<Login>>

    // Create Profile
    @POST("/api/profile")
    fun register(@Body register: RegisterRequest): Call<ApiResponse<Login>>
}