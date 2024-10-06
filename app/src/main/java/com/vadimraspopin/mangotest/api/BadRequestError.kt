package com.vadimraspopin.mangotest.api

data class BadRequestResponse(
    val detail: BadRequestDetail
)

data class BadRequestDetail(
    val message: String,
)

class BadRequestException(val error: BadRequestResponse) : Exception()