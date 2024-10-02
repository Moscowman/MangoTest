package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.models.AuthResponse

interface AuthRepository {
    suspend fun sendAuthCode(phone: String)
    suspend fun checkAuthCode(phone: String, code: String): AuthResponse
}