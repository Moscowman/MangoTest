package com.vadimraspopin.mangotest.api

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenProvider: TokenProvider,
    private val api: AuthApiService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            // Проверяем, не был ли токен уже обновлён другим потоком
            val newAccessToken = tokenProvider.getAccessToken()
            if (response.request().header("Authorization") == "Bearer $newAccessToken") {
                val refreshToken = tokenProvider.getRefreshToken() ?: return null

                val refreshResponse = runBlocking {
                    api.refreshToken(RefreshTokenRequest(refreshToken))
                }

                return if (refreshResponse.isSuccessful) {
                    val newTokens = refreshResponse.body() ?: return null
                    tokenProvider.saveTokens(newTokens.accessToken, newTokens.refreshToken)
                    response.request().newBuilder()
                        .header("Authorization", "Bearer ${newTokens.accessToken}")
                        .build()
                } else {
                    null
                }
            }
            return null
        }
    }
}