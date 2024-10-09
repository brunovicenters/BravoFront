package com.example.bravofront.api

import com.example.bravofront.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CartAPI {

    @POST("/api/carrinho")
    fun store(@Body produto: CartInsertRequest): Call<ApiResponse<CartInsert>>

}