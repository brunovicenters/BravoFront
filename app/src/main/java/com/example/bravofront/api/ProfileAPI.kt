package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileAPI {

    // Create Profile
    @POST("/api/profile")
    fun register(@Body register: RegisterRequest): Call<ApiResponse<Login>>
}