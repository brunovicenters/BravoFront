package com.example.bravofront.api

import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.StorePedidoRequest
import com.example.bravofront.model.StorePedidoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PedidoAPI {
    @POST("/api/pedido")
    fun create(@Body pedido: StorePedidoRequest): Call<ApiResponse<StorePedidoResponse>>
}