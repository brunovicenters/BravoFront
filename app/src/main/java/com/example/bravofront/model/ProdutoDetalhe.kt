package com.example.bravofront.model

data class ProdutoDetalhe(
    val id: Int,
    val nome: String,
    val preco: String,
    val desconto: String?,
    val categoria: String,
    val qtd: Int,
    val desc: String?,
    val imagem: List<Image>?
)
