package com.vadimraspopin.mangotest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.models.AuthResponse
import com.vadimraspopin.mangotest.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(val authRepository: AuthRepository) : ViewModel() {

    var phone by mutableStateOf(TextFieldValue())
    var code by mutableStateOf(TextFieldValue())

    fun sendAuthCode(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.sendAuthCode(phone.text)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun checkAuthCode(onSuccess: (AuthResponse) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = authRepository.checkAuthCode(phone.text, code.text)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}