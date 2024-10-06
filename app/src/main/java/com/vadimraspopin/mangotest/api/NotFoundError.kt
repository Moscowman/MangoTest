package com.vadimraspopin.mangotest.api

data class NotFoundErrorResponse(
    val detail: NotFoundErrorDetail
)

data class NotFoundErrorDetail(
    val message: String,
)

class NotFoundException(val error: NotFoundErrorResponse) : Exception()