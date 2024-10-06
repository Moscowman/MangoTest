package com.vadimraspopin.mangotest.api.errors

data class NotFoundErrorResponse(
    val detail: NotFoundErrorDetail
)

data class NotFoundErrorDetail(
    val message: String,
)

class NotFoundException(val error: NotFoundErrorResponse) : Exception()