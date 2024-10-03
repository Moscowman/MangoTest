package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.SendAuthCodeResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun sendAuthCode(phone: String): Flow<SendAuthCodeResponse>
    fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponse>
}