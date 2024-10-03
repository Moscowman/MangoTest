package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.SendAuthCodeResponseDto
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    fun sendAuthCode(phone: String): Flow<SendAuthCodeResponseDto>
    fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponseDto>
}