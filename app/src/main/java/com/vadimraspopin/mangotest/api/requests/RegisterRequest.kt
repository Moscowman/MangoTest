package com.vadimraspopin.mangotest.api.requests

data class RegisterRequest(
    val phone: String,
    val name: String,
    val username: String
)
