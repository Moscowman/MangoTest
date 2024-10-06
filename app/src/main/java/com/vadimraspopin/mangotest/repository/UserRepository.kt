package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.services.ProfileApiService
import com.vadimraspopin.mangotest.datasource.UserPreferences
import com.vadimraspopin.mangotest.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ProfileApiService, private val userPreferences: UserPreferences
) {

    fun fetchUser(): Flow<Result<User>> = flow {
        try {
            val user = apiService.getMyProfile()
            userPreferences.saveUser(user)
            emit(Result.success(user))
        } catch (e: Exception) {
            val localUser = userPreferences.userFlow.first()
            if (localUser != null) {
                emit(Result.success(localUser))
            } else {
                emit(Result.failure(e))
            }
        }
    }

    fun getCachedUser(): Flow<User?> = userPreferences.userFlow
}