package com.vadimraspopin.mangotest.di

import com.vadimraspopin.mangotest.api.services.AuthApiService
import com.vadimraspopin.mangotest.api.interceptors.AuthInterceptor
import com.vadimraspopin.mangotest.api.services.ProfileApiService
import com.vadimraspopin.mangotest.api.authenticators.TokenAuthenticator
import com.vadimraspopin.mangotest.api.providers.TokenProvider
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
    fun provideApiService(tokenProvider: TokenProvider): Retrofit {

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

        return retrofit
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }
}