package com.example.bravofront.model

data class StorePedidoRequest(
    val endereco: Int,
    val produtos: MutableList<ProdutoPedidoRequest>
)
