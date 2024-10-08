package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.mappers.toDomainModel
import com.vadimraspopin.mangotest.api.providers.TokenProvider
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSource
import com.vadimraspopin.mangotest.datasource.ProfilePreferences
import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.RegisterResponse
import com.vadimraspopin.mangotest.model.SendAuthCodeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val profilePreferences: ProfilePreferences,
    private val tokenProvider: TokenProvider
) :
    AuthRepository {
    override fun sendAuthCode(phone: String): Flow<SendAuthCodeResponse> =
        remoteDataSource.sendAuthCode(phone)
            .map { responseDto ->
                responseDto.toDomainModel()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponse> =
        remoteDataSource.checkAuthCode(phone, code)
            .map { responseDto ->
                responseDto.toDomainModel()
            }
            .flatMapConcat { response ->
                val cachedUser = profilePreferences.profileFlow.firstOrNull()
                if (cachedUser?.id?.toLong() != response.userId) {
                    profilePreferences.clearProfile()
                }

                if (response.isUserExists) {
                    flow {
                        if (response.accessToken != null && response.refreshToken != null) {
                            tokenProvider.saveTokens(response.accessToken, response.refreshToken)
                        }
                        emit(response)
                    }.flowOn(Dispatchers.IO)
                } else {
                    flowOf(response)
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun registerUser(
        phone: String,
        name: String,
        username: String
    ): Flow<RegisterResponse> {
        return remoteDataSource.register(phone, name, username)
            .map { registerDto ->
                registerDto.toDomainModel()
            }
            .flatMapConcat { registerResponse ->
                flow {
                    tokenProvider.saveTokens(
                        registerResponse.accessToken,
                        registerResponse.refreshToken
                    )
                    emit(registerResponse)
                }
            }
    }
}