package com.vadimraspopin.mangotest.model

data class RegisterResponse(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long
)
