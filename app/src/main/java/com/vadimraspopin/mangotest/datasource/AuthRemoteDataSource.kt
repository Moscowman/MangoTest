package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.AuthResponseDto
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    fun sendAuthCode(phone: String): Flow<Unit>
    fun checkAuthCode(phone: String, code: Int): Flow<AuthResponseDto>
}