package com.vadimraspopin.mangotest.models

data class AuthResponse(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long,
    val isUserExists: Boolean
)