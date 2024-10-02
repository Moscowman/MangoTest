package com.vadimraspopin.mangotest.api

import com.vadimraspopin.mangotest.models.AuthResponse

fun AuthResponseDto.toDomainModel(): AuthResponse {
    return AuthResponse(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken,
        userId = this.userId,
        isUserExists = this.isUserExists
    )
}