package com.vadimraspopin.mangotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vadimraspopin.mangotest.api.AuthApiService
import com.vadimraspopin.mangotest.datasource.AuthRemoteDataSourceImpl
import com.vadimraspopin.mangotest.repository.AuthRepositoryImpl
import com.vadimraspopin.mangotest.ui.AuthorizationScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://your.api.url/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(AuthApiService::class.java)
        val authRemoteDataSource = AuthRemoteDataSourceImpl(apiService)
        val authRepository = AuthRepositoryImpl(authRemoteDataSource)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
            AuthorizationScreen(authViewModel)
        }
    }
}