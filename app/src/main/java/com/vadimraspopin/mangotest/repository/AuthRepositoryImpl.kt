package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.toDomainModel
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSource
import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.SendAuthCodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val remoteDataSource: AuthRemoteDataSource) :
    AuthRepository {
    override fun sendAuthCode(phone: String): Flow<SendAuthCodeResponse> =
        remoteDataSource.sendAuthCode(phone)
            .map { responseDto ->
                responseDto.toDomainModel()
            }

    override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponse> =
        remoteDataSource.checkAuthCode(phone, code)
            .map { responseDto ->
                responseDto.toDomainModel()
            }
}