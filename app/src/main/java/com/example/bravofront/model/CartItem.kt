package com.example.bravofront.model

data class CartItem(
    val id: Int,
    val name: String,
    val quantity: Int,
    val stock: Int,
    val price: String,
    val image: String?,
    val changedQty: Boolean
)
