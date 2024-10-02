package com.vadimraspopin.mangotest.api

data class AuthResponseDto(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long,
    val isUserExists: Boolean
)