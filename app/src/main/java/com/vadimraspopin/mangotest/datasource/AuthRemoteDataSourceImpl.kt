package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.AuthApiService
import com.vadimraspopin.mangotest.api.AuthResponseDto
import com.vadimraspopin.mangotest.api.CheckAuthCodeRequest
import com.vadimraspopin.mangotest.api.SendAuthCodeRequest

class AuthRemoteDataSourceImpl(private val apiService: AuthApiService) : AuthRemoteDataSource {
    override suspend fun sendAuthCode(phone: String) {
        apiService.sendAuthCode(SendAuthCodeRequest(phone))
    }

    override suspend fun checkAuthCode(phone: String, code: String): AuthResponseDto {
        return apiService.checkAuthCode(CheckAuthCodeRequest(phone, code))
    }
}