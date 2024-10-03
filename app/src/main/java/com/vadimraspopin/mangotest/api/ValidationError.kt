package com.vadimraspopin.mangotest.api

data class ValidationErrorResponse(
    val detail: ValidationErrorDetail
)

data class ValidationErrorDetail(
    val message: String,
)

class ValidationException(val error: ValidationErrorResponse) : Exception()