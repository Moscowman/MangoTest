package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.responses.UserResponseDto
import kotlinx.coroutines.flow.Flow

interface ProfileRemoteDataSource {
    fun getMyProfile(): Flow<UserResponseDto>
}