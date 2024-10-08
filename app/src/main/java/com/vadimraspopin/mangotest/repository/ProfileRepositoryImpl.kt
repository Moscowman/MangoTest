package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.mappers.toDomainModel
import com.vadimraspopin.mangotest.datasource.ProfilePreferences
import com.vadimraspopin.mangotest.datasource.ProfileRemoteDataSource
import com.vadimraspopin.mangotest.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profilePreferences: ProfilePreferences
) : ProfileRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(): Flow<User> =
        profilePreferences.profileFlow.flatMapLatest { cachedUser ->
            if (cachedUser != null) {
                flowOf(cachedUser)
            } else {
                fetchUser()
            }
        }

    override fun fetchUser(): Flow<User> =
        profileRemoteDataSource.getMyProfile()
            .map { responseDto ->
                responseDto.toDomainModel()
            }
            .onEach { user: User ->
                profilePreferences.saveProfile(user)
            }
            .flowOn(Dispatchers.IO)

    override fun getCachedUser(): Flow<User?> = profilePreferences.profileFlow

    override suspend fun clearCache() {
        profilePreferences.clearProfile()
    }
}