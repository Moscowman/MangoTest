package com.vadimraspopin.mangotest.api

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