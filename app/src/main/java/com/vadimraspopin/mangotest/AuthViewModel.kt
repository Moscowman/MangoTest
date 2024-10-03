package com.vadimraspopin.mangotest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.models.AuthResponse
import com.vadimraspopin.mangotest.repository.AuthRepository
import com.vadimraspopin.mangotest.ui.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {

    var phone by mutableStateOf("")
    var code by mutableStateOf(0)

    private val _authState = MutableStateFlow<AuthUiState<Any>>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState<Any>> = _authState.asStateFlow()

    fun sendAuthCode() {
        viewModelScope.launch {
            authRepository.sendAuthCode(phone)
                .onStart {
                    _authState.value = AuthUiState.Loading
                }
                .catch { e ->
                    _authState.value = AuthUiState.Error(e.message ?: "Unknown error")
                }
                .collect {
                    _authState.value = AuthUiState.Success(Unit)
                }
        }
    }

    fun checkAuthCode() {
        viewModelScope.launch {
            authRepository.checkAuthCode(phone, code)
                .onStart {
                    _authState.value = AuthUiState.Loading
                }
                .catch { e ->
                    _authState.value = AuthUiState.Error(e.message ?: "Unknown error")
                }
                .collect { response ->
                    _authState.value = AuthUiState.Success(response)
                }
        }
    }
}