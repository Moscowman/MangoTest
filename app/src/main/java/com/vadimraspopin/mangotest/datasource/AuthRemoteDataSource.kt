package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.AuthResponseDto

interface AuthRemoteDataSource {
    suspend fun sendAuthCode(phone: String)
    suspend fun checkAuthCode(phone: String, code: String): AuthResponseDto
}