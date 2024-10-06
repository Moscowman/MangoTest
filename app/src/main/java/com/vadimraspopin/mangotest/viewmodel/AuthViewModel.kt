package com.vadimraspopin.mangotest.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.api.NotFoundException
import com.vadimraspopin.mangotest.api.ValidationException
import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.model.SendAuthCodeResponse
import com.vadimraspopin.mangotest.repository.AuthRepository
import com.vadimraspopin.mangotest.ui.ApiUiRequestState
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

    var phoneNumber = mutableStateOf("")
    var code = mutableStateOf("")
    var fullPhoneNumber: String? = null

    private val _sendAuthCodeState =
        MutableStateFlow<ApiUiRequestState<SendAuthCodeResponse>>(ApiUiRequestState.Idle)
    val sendAuthCodeState: StateFlow<ApiUiRequestState<SendAuthCodeResponse>> =
        _sendAuthCodeState.asStateFlow()

    private val _checkAuthCodeState =
        MutableStateFlow<ApiUiRequestState<CheckAuthCodeResponse>>(ApiUiRequestState.Idle)
    val checkAuthCodeState: StateFlow<ApiUiRequestState<CheckAuthCodeResponse>> =
        _checkAuthCodeState.asStateFlow()

    fun sendAuthCode() {
        if (fullPhoneNumber == null) return
        viewModelScope.launch {
            authRepository.sendAuthCode(fullPhoneNumber!!)
                .onStart {
                    _sendAuthCodeState.value = ApiUiRequestState.Loading
                }
                .catch { e ->
                    when (e) {
                        is ValidationException ->
                            _sendAuthCodeState.value =
                                ApiUiRequestState.Error(
                                    e.error.detail.map { it.msg }
                                )

                        else ->
                            _sendAuthCodeState.value =
                                ApiUiRequestState.Error(listOf(e.message ?: "Unknown error"))
                    }
                }
                .collect { value ->
                    _sendAuthCodeState.value = ApiUiRequestState.Success(value)
                }
        }
    }

    fun checkAuthCode() {
        if (fullPhoneNumber == null) return
        viewModelScope.launch {
            authRepository.checkAuthCode(fullPhoneNumber!!, code.value)
                .onStart {
                    _checkAuthCodeState.value = ApiUiRequestState.Loading
                }
                .catch { e ->
                    when (e) {
                        is ValidationException ->
                            _checkAuthCodeState.value =
                                ApiUiRequestState.Error(
                                    e.error.detail.map { it.msg }
                                )

                        is NotFoundException ->
                            _checkAuthCodeState.value =
                                ApiUiRequestState.Error(
                                    listOf(e.error.detail.message)
                                )

                        else ->
                            _checkAuthCodeState.value =
                                ApiUiRequestState.Error(listOf(e.message ?: "Unknown error"))
                    }
                }
                .collect { response ->
                    _checkAuthCodeState.value = ApiUiRequestState.Success(response)
                }
        }
    }

    fun resetAuthCode() {
        _sendAuthCodeState.value = ApiUiRequestState.Idle
        _checkAuthCodeState.value = ApiUiRequestState.Idle
        code.value = ""
    }
}