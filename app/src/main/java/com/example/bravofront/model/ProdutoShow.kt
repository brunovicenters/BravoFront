package com.example.bravofront.model

data class ProdutoShow(
    val produto: ProdutoDetalhe,
    val semelhantes: List<ProdutoIndex>?
)