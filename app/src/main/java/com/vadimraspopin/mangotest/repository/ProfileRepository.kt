package com.vadimraspopin.mangotest.repository

import com.vadimraspopin.mangotest.api.requests.ProfileUpdateRequest
import com.vadimraspopin.mangotest.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUser(): Flow<User> = fetchUser()

    fun fetchUser(): Flow<User>

    fun getCachedUser(): Flow<User?>

    suspend fun clearCache()

    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): Flow<User>
}