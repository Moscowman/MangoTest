package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.RegisterResponse
import com.vadimraspopin.mangotest.model.SendAuthCodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository : AuthRepository {
    override fun sendAuthCode(phone: String): Flow<SendAuthCodeResponse> {
        return TODO()
    }

    override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponse> {
        return flow {
            emit(
                CheckAuthCodeResponse(
                    refreshToken = "",
                    accessToken = "",
                    userId = 0,
                    isUserExists = false
                )
            )
        }
    }

    override fun registerUser(
        phone: String,
        name: String,
        username: String
    ): Flow<RegisterResponse> {
        TODO()
    }
}