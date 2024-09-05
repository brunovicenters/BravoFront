package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Home
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.model.Search
import retrofit2.Call
import retrofit2.http.GET

interface SearchAPI {
    @GET("/api/produto")
    // TODO: Fix response
    fun index(): Call<ApiResponse<Search>>
}