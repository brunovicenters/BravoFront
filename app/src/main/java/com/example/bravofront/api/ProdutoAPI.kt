package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.ProdutoShow
import com.example.bravofront.model.Search
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProdutoAPI {
    @GET("/api/produto")
    fun index(): Call<ApiResponse<Search>>

    @GET("/api/produto/{id}")
    fun show(@Path("id") id: Int, @Query("user") user: Int?): Call<ApiResponse<ProdutoShow>>
}