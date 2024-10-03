package com.vadimraspopin.mangotest.model

data class CheckAuthCodeResponse(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long,
    val isUserExists: Boolean
)