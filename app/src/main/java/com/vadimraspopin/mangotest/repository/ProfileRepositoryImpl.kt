package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.mappers.toDomainModel
import com.vadimraspopin.mangotest.datasource.ProfileRemoteDataSource
import com.vadimraspopin.mangotest.datasource.UserPreferences
import com.vadimraspopin.mangotest.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val userPreferences: UserPreferences
) : ProfileRepository {

    override fun getUser() = fetchUser()

    override fun fetchUser(): Flow<User> =
        profileRemoteDataSource.getMyProfile()
            .map { responseDto ->
                responseDto.toDomainModel()
            }

    override fun getCachedUser(): Flow<User?> = userPreferences.userFlow
}