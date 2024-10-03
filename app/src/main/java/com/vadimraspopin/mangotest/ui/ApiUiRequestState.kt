package com.vadimraspopin.mangotest.ui

sealed class ApiUiRequestState<out T> {
    object Idle : ApiUiRequestState<Nothing>()
    object Loading : ApiUiRequestState<Nothing>()
    data class Success<T>(val data: T) : ApiUiRequestState<T>()
    data class Error(val message: String) : ApiUiRequestState<Nothing>()
}