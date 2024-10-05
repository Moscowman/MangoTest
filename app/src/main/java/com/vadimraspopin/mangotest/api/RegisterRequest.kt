package com.vadimraspopin.mangotest.api

data class RegisterRequest(
    val phone: String,
    val name: String,
    val username: String
)
