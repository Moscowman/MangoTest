package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.responses.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.responses.RegisterResponseDto
import com.vadimraspopin.mangotest.api.responses.SendAuthCodeResponseDto
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    fun sendAuthCode(phone: String): Flow<SendAuthCodeResponseDto>
    fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponseDto>
    fun register(phone: String, name: String, username: String): Flow<RegisterResponseDto>
}