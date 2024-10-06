package com.vadimraspopin.mangotest.api

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(accessToken: String, refreshToken: String)
}