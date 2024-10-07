package com.vadimraspopin.mangotest.datasource

import com.google.gson.Gson
import com.vadimraspopin.mangotest.api.services.AuthApiService
import com.vadimraspopin.mangotest.api.errors.BadRequestException
import com.vadimraspopin.mangotest.api.errors.BadRequestResponse
import com.vadimraspopin.mangotest.api.requests.CheckAuthCodeRequest
import com.vadimraspopin.mangotest.api.responses.CheckAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.errors.NotFoundErrorResponse
import com.vadimraspopin.mangotest.api.errors.NotFoundException
import com.vadimraspopin.mangotest.api.requests.RegisterRequest
import com.vadimraspopin.mangotest.api.responses.RegisterResponseDto
import com.vadimraspopin.mangotest.api.requests.SendAuthCodeRequest
import com.vadimraspopin.mangotest.api.responses.SendAuthCodeResponseDto
import com.vadimraspopin.mangotest.api.errors.ValidationErrorResponse
import com.vadimraspopin.mangotest.api.errors.ValidationException
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
            when (response.code()) {
                422 -> {
                    val errorBody = response.errorBody()?.string()
                    val validationError =
                        gson.fromJson(errorBody, ValidationErrorResponse::class.java)
                    throw ValidationException(validationError)
                }
            }
        }
    }

    override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponseDto> =
        flow {
            val response = apiService.checkAuthCode(CheckAuthCodeRequest(phone, code))
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
            when (response.code()) {
                400 -> {
                    val errorBody = response.errorBody()?.string()
                    val validationError =
                        gson.fromJson(errorBody, BadRequestResponse::class.java)
                    throw BadRequestException(validationError)
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
}