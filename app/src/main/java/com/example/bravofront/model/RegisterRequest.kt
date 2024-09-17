package com.example.bravofront.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val cpf: String,
    val password: String,
    val passwordConfirmation: String
)
