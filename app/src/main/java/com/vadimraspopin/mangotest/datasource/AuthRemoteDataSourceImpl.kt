package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.AuthApiService
import com.vadimraspopin.mangotest.api.CheckAuthCodeRequest
import com.vadimraspopin.mangotest.api.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.SendAuthCodeRequest
import com.vadimraspopin.mangotest.api.SendAuthCodeResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRemoteDataSourceImpl(private val apiService: AuthApiService) : AuthRemoteDataSource {
    override fun sendAuthCode(phone: String): Flow<SendAuthCodeResponseDto> = flow {
        val response = apiService.sendAuthCode(SendAuthCodeRequest(phone))
        emit(response)
    }

    override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponseDto> = flow {
        val response = apiService.checkAuthCode(CheckAuthCodeRequest(phone, code))
        emit(response)
    }
}