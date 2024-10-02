package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.toDomainModel
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSource
import com.vadimraspopin.mangotest.models.AuthResponse

class AuthRepositoryImpl(private val remoteDataSource: AuthRemoteDataSource) : AuthRepository {
    override suspend fun sendAuthCode(phone: String) {
        remoteDataSource.sendAuthCode(phone)
    }

    override suspend fun checkAuthCode(phone: String, code: String): AuthResponse {
        val responseDto = remoteDataSource.checkAuthCode(phone, code)
        return responseDto.toDomainModel()
    }
}