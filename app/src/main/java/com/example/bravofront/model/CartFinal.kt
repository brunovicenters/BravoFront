package com.example.bravofront.model

data class CartFinal(
    val produtos: List<CartFinalItem>,
    val enderecos: List<FinalCartEnderecos>
)
