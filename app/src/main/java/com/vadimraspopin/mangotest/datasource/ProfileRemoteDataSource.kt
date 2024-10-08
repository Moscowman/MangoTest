package com.vadimraspopin.mangotest.datasource

import com.vadimraspopin.mangotest.api.requests.ProfileUpdateRequest
import com.vadimraspopin.mangotest.api.responses.ProfileUpdateResponseDto
import com.vadimraspopin.mangotest.api.responses.UserResponseDto
import kotlinx.coroutines.flow.Flow

interface ProfileRemoteDataSource {
    fun getMyProfile(): Flow<UserResponseDto>
    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): Flow<ProfileUpdateResponseDto>
}