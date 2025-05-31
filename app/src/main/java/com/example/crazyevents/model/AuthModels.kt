package com.example.crazyevents.model

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String
)