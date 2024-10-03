package com.vadimraspopin.mangotest.api

import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.SendAuthCodeResponse

fun CheckAuthCodeResponseDto.toDomainModel(): CheckAuthCodeResponse {
    return CheckAuthCodeResponse(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken,
        userId = this.userId,
        isUserExists = this.isUserExists
    )
}

fun SendAuthCodeResponseDto.toDomainModel(): SendAuthCodeResponse {
    return SendAuthCodeResponse(
        isSuccess = this.isSuccess
    )
}