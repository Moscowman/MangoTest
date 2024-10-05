package com.vadimraspopin.mangotest.datasource

import com.google.gson.Gson
import com.vadimraspopin.mangotest.api.AuthApiService
import com.vadimraspopin.mangotest.api.CheckAuthCodeRequest
import com.vadimraspopin.mangotest.api.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.RegisterRequest
import com.vadimraspopin.mangotest.api.RegisterResponseDto
import com.vadimraspopin.mangotest.api.SendAuthCodeRequest
import com.vadimraspopin.mangotest.api.SendAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.ValidationErrorResponse
import com.vadimraspopin.mangotest.api.ValidationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRemoteDataSourceImpl(private val apiService: AuthApiService) : AuthRemoteDataSource {

    val gson = Gson()

    override fun sendAuthCode(phone: String): Flow<SendAuthCodeResponseDto> = flow {
        val response = apiService.sendAuthCode(SendAuthCodeRequest(phone))
        if (response.isSuccessful) {
            response.body()?.let { body ->
                emit(body)
            } ?: throw Exception("Пустое тело ответа")
        } else {
            val errorBody = response.errorBody()?.string()
            val validationError = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
            throw ValidationException(validationError)
        }
    }

    override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponseDto> = flow {
        val response = apiService.checkAuthCode(CheckAuthCodeRequest(phone, code))
        if (response.isSuccessful) {
            response.body()?.let { body ->
                emit(body)
            } ?: throw Exception("Пустое тело ответа")
        } else {
            val errorBody = response.errorBody()?.string()
            val validationError = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
            throw ValidationException(validationError)
        }
    }

    override fun register(
        phone: String,
        name: String,
        username: String
    ): Flow<RegisterResponseDto> = flow {
        val response = apiService.register(RegisterRequest(phone, name, username))
        if (response.isSuccessful) {
            response.body()?.let { body ->
                emit(body)
            } ?: throw Exception("Пустое тело ответа")
        } else {
            val errorBody = response.errorBody()?.string()
            val validationError = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
            throw ValidationException(validationError)
        }
    }
}