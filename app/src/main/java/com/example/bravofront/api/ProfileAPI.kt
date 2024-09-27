package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.ProfileShow
import com.example.bravofront.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileAPI {

    // Show Profile
    @GET("/api/profile/{id}")
    fun show(@Path("id") id: String): Call<ApiResponse<ProfileShow>>
}