package com.vadimraspopin.mangotest.api

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseDto(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
        @SerializedName("user_id") val userId: String
)
