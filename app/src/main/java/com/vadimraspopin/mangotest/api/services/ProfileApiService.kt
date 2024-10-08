package com.vadimraspopin.mangotest.api.services

import com.vadimraspopin.mangotest.api.requests.ProfileUpdateRequest
import com.vadimraspopin.mangotest.api.responses.ProfileUpdateResponseDto
import com.vadimraspopin.mangotest.api.responses.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT


interface ProfileApiService {
    @GET("/api/v1/users/me/")
    suspend fun getMyProfile(): Response<UserResponseDto>

    @PUT("/api/v1/users/me/")
    suspend fun updateMyProfile(@Body profileUpdateRequest: ProfileUpdateRequest): Response<ProfileUpdateResponseDto>
}