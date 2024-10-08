package com.vadimraspopin.mangotest.datasource

import com.google.gson.Gson
import com.vadimraspopin.mangotest.api.errors.NotFoundErrorResponse
import com.vadimraspopin.mangotest.api.errors.NotFoundException
import com.vadimraspopin.mangotest.api.errors.ValidationErrorResponse
import com.vadimraspopin.mangotest.api.errors.ValidationException
import com.vadimraspopin.mangotest.api.requests.ProfileUpdateRequest
import com.vadimraspopin.mangotest.api.responses.ProfileUpdateResponseDto
import com.vadimraspopin.mangotest.api.responses.UserResponseDto
import com.vadimraspopin.mangotest.api.services.ProfileApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRemoteDataSourceImpl @Inject constructor(
    private val apiService: ProfileApiService,
    private val gson: Gson
): ProfileRemoteDataSource {

    override fun getMyProfile(): Flow<UserResponseDto> = flow {
        val response = apiService.getMyProfile()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                emit(body)
            } ?: throw Exception("Пустое тело ответа")
        } else {
            when (response.code()) {
                404 -> {
                    val errorBody = response.errorBody()?.string()
                    val validationError =
                        gson.fromJson(errorBody, NotFoundErrorResponse::class.java)

                    throw NotFoundException(validationError)
                }

                422 -> {
                    val errorBody = response.errorBody()?.string()
                    val validationError =
                        gson.fromJson(errorBody, ValidationErrorResponse::class.java)

                    throw ValidationException(validationError)
                }
            }
        }
    }

    override fun updateProfile(
        profileUpdateRequest: ProfileUpdateRequest
    ): Flow<ProfileUpdateResponseDto> = flow {
        val response = apiService.updateMyProfile(profileUpdateRequest)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                emit(body)
            } ?: throw Exception("Пустое тело ответа")
        } else {
            val errorBody = response.errorBody()?.string()
            when (response.code()) {
                400, 422 -> {
                    val validationError =
                        gson.fromJson(errorBody, ValidationErrorResponse::class.java)
                    throw ValidationException(validationError)
                }
                404 -> {
                    val notFoundError =
                        gson.fromJson(errorBody, NotFoundErrorResponse::class.java)
                    throw NotFoundException(notFoundError)
                }
                else -> {
                    throw Exception("Произошла ошибка: ${response.code()} ${response.message()}")
                }
            }
        }
    }
}