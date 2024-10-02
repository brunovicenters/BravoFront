package com.example.bravofront.model

data class ProdutoIndex(
	val preco: String = "10000.00",
	val desconto: String? = null,
	val imagem: String? = null,
	val nome: String = "Carro",
	val id: Int = -1
)
