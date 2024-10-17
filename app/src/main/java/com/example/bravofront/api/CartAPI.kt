package com.example.bravofront.api

import com.example.bravofront.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartAPI {

    @GET("/api/carrinho")
    fun index(): Call<ApiResponse<CartIndex>>


    @POST("/api/carrinho")
    fun store(@Body produto: CartInsertRequest): Call<ApiResponse<CartInsert>>

    @PUT("/api/carrinho/{id}")
    fun update(@Body item: CartUpdateRequest, @Path("id") id: Int): Call<ApiResponse<CartUpdate>>

}