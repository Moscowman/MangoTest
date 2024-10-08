package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.model.User
import kotlinx.coroutines.flow.Flow

interface ProfilePreferences {
    val profileFlow: Flow<User?>
    suspend fun saveProfile(user: User)
    suspend fun clearProfile()
}