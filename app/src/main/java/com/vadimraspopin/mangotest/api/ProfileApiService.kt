package com.vadimraspopin.mangotest.api

import com.vadimraspopin.mangotest.model.User
import retrofit2.http.Body
import retrofit2.http.GET


interface ProfileApiService {
    @GET("/api/v1/users/me/")
    suspend fun getMyProfile(@Body request: GetMyProfileRequest): User
}