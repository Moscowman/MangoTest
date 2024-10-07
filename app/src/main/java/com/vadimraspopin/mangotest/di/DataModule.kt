package com.vadimraspopin.mangotest.di

import com.google.gson.Gson
import com.vadimraspopin.mangotest.BuildConfig
import com.vadimraspopin.mangotest.api.authenticators.TokenAuthenticator
import com.vadimraspopin.mangotest.api.interceptors.AuthInterceptor
import com.vadimraspopin.mangotest.api.providers.TokenProvider
import com.vadimraspopin.mangotest.api.services.AuthApiService
import com.vadimraspopin.mangotest.api.services.ProfileApiService
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSource
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSourceImpl
import com.vadimraspopin.mangotest.datasource.ProfileRemoteDataSource
import com.vadimraspopin.mangotest.datasource.ProfileRemoteDataSourceImpl
import com.vadimraspopin.mangotest.datasource.UserPreferences
import com.vadimraspopin.mangotest.repository.AuthRepository
import com.vadimraspopin.mangotest.repository.AuthRepositoryImpl
import com.vadimraspopin.mangotest.repository.ProfileRepository
import com.vadimraspopin.mangotest.repository.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        dataSource: AuthRemoteDataSource,
        tokenProvider: TokenProvider
    ): AuthRepository {
        return AuthRepositoryImpl(dataSource, tokenProvider)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        authApiService: AuthApiService,
        gson: Gson
    ): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(authApiService, gson)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        dataSource: ProfileRemoteDataSource,
        userPreferences: UserPreferences
    ): ProfileRepository {
        return ProfileRepositoryImpl(dataSource, userPreferences)
    }

    @Provides
    @Singleton
    fun provideProfileRemoteDataSource(
        profileApiService: ProfileApiService,
        gson: Gson
    ): ProfileRemoteDataSource {
        return ProfileRemoteDataSourceImpl(profileApiService, gson)
    }

    @Provides
    @Singleton
    fun provideApiService(
        tokenProvider: TokenProvider,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): Retrofit {

        val authApi = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
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

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message ->
            android.util.Log.d("OkHttp", message)
        }
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}