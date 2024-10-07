package com.vadimraspopin.mangotest.api.services

import com.vadimraspopin.mangotest.api.responses.UserResponseDto
import retrofit2.Response
import retrofit2.http.GET


interface ProfileApiService {
    @GET("/api/v1/users/me/")
    suspend fun getMyProfile(): Response<UserResponseDto>
}