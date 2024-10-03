package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun sendAuthCode(phone: String): Flow<Unit>
    fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponse>
}