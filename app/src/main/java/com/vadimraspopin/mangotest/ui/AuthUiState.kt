package com.vadimraspopin.mangotest.ui

sealed class AuthUiState<out T> {
    object Idle : AuthUiState<Nothing>()
    object Loading : AuthUiState<Nothing>()
    data class Success<T>(val data: T) : AuthUiState<T>()
    data class Error(val message: String) : AuthUiState<Nothing>()
}