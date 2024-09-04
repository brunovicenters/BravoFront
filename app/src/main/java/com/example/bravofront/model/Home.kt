package com.example.bravofront.model

data class Home(
    val categoriasMaisVendidas: List<CategoriaMaisVendida>,
    val produtosMaisVendidos: List<ProdutoIndex>,
    val promocao: List<ProdutoIndex>
)
