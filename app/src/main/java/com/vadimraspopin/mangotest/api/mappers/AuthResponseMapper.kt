package com.vadimraspopin.mangotest.api.mappers

import com.vadimraspopin.mangotest.api.responses.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.responses.RegisterResponseDto
import com.vadimraspopin.mangotest.api.responses.SendAuthCodeResponseDto
import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.RegisterResponse
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

fun RegisterResponseDto.toDomainModel(): RegisterResponse {
    return RegisterResponse(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken,
        userId = this.userId
    )
}