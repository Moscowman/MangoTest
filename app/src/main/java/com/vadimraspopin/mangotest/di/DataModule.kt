package com.vadimraspopin.mangotest.di

import com.vadimraspopin.mangotest.api.AuthApiService
import com.vadimraspopin.mangotest.api.AuthInterceptor
import com.vadimraspopin.mangotest.api.TokenAuthenticator
import com.vadimraspopin.mangotest.api.TokenProvider
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSource
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSourceImpl
import com.vadimraspopin.mangotest.repository.AuthRepository
import com.vadimraspopin.mangotest.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_API_URL = "https://plannerok.ru/"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        dataSource: AuthRemoteDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(authApiService: AuthApiService): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(authApiService)
    }

    @Provides
    @Singleton
    fun provideApiService(tokenProvider: TokenProvider): AuthApiService {

        val authApi = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .authenticator(TokenAuthenticator(tokenProvider, authApi))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApiService::class.java)
    }
}