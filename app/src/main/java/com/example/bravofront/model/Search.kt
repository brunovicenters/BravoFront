package com.example.bravofront.model

data class Search(
    val produtos: List<ProdutoIndex>,
    val maisVendido: List<ProdutoIndex>,
    val categorias: List<CategoriaSimples>
)
