package com.vadimraspopin.mangotest.api

data class ValidationErrorResponse(
    val detail: List<ValidationErrorDetail>
)

data class ValidationErrorDetail(
    val loc: List<String>,
    val msg: String,
    val type: String
)

class ValidationException(val error: ValidationErrorResponse) : Exception()