package com.vadimraspopin.mangotest.di

import com.vadimraspopin.mangotest.api.DataStoreTokenProvider
import com.vadimraspopin.mangotest.api.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(
        dataStoreTokenProvider: DataStoreTokenProvider
    ): TokenProvider
}