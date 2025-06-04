package com.example.crazyevents.model

import com.example.crazyevents.data.UserProfile

data class AuthRequest(
    val name: String? = null,
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: UserProfile
)