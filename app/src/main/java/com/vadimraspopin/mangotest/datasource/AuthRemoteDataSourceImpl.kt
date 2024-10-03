package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.AuthApiService
import com.vadimraspopin.mangotest.api.AuthResponseDto
import com.vadimraspopin.mangotest.api.CheckAuthCodeRequest
import com.vadimraspopin.mangotest.api.SendAuthCodeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRemoteDataSourceImpl(private val apiService: AuthApiService) : AuthRemoteDataSource {
    override fun sendAuthCode(phone: String): Flow<Unit> = flow {
        apiService.sendAuthCode(SendAuthCodeRequest(phone))
        emit(Unit)
    }

    override fun checkAuthCode(phone: String, code: Int): Flow<AuthResponseDto> = flow {
        val response = apiService.checkAuthCode(CheckAuthCodeRequest(phone, code))
        emit(response)
    }
}