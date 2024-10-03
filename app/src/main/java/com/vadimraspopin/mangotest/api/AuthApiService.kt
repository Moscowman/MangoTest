package com.vadimraspopin.mangotest.api

import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthCodeRequest): SendAuthCodeResponseDto

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CheckAuthCodeRequest): CheckAuthCodeResponseDto
}