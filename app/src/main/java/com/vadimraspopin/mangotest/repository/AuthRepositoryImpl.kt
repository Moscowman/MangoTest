package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.toDomainModel
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSource
import com.vadimraspopin.mangotest.models.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: AuthRemoteDataSource) :
    AuthRepository {
    override fun sendAuthCode(phone: String): Flow<Unit> =
        remoteDataSource.sendAuthCode(phone)

    override fun checkAuthCode(phone: String, code: Int): Flow<AuthResponse> =
        remoteDataSource.checkAuthCode(phone, code)
            .map { responseDto ->
                responseDto.toDomainModel()
            }
}