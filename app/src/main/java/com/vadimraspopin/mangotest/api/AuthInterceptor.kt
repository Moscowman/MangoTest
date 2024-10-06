package com.vadimraspopin.mangotest.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        tokenProvider.getAccessToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}