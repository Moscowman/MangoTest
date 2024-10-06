package com.vadimraspopin.mangotest.api.responses

data class CheckAuthCodeResponseDto(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long,
    val isUserExists: Boolean
)