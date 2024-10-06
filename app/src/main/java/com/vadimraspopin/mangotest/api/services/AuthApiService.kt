package com.vadimraspopin.mangotest.api.services

import com.vadimraspopin.mangotest.api.requests.CheckAuthCodeRequest
import com.vadimraspopin.mangotest.api.responses.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.requests.RefreshTokenRequest
import com.vadimraspopin.mangotest.api.responses.RefreshTokenResponseDto
import com.vadimraspopin.mangotest.api.requests.RegisterRequest
import com.vadimraspopin.mangotest.api.responses.RegisterResponseDto
import com.vadimraspopin.mangotest.api.requests.SendAuthCodeRequest
import com.vadimraspopin.mangotest.api.responses.SendAuthCodeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthCodeRequest): Response<SendAuthCodeResponseDto>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CheckAuthCodeRequest): Response<CheckAuthCodeResponseDto>

    @POST("/api/v1/users/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponseDto>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponseDto>
}