package com.vadimraspopin.mangotest.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.api.ValidationException
import com.vadimraspopin.mangotest.model.RegisterResponse
import com.vadimraspopin.mangotest.repository.AuthRepository
import com.vadimraspopin.mangotest.ui.ApiUiRequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userRepository: AuthRepository) : ViewModel() {

    var name = mutableStateOf("")

    var username = mutableStateOf("")

    private val _registerState =
        MutableStateFlow<ApiUiRequestState<RegisterResponse>>(ApiUiRequestState.Idle)

    val registerState: StateFlow<ApiUiRequestState<RegisterResponse>> = _registerState

    fun register(phone: String) {
        viewModelScope.launch {
            _registerState.value = ApiUiRequestState.Loading
            userRepository.registerUser(phone, name.value, username.value)
                .onStart {
                    _registerState.value = ApiUiRequestState.Loading
                }
                .catch { e ->
                    when (e) {
                        is ValidationException ->
                            _registerState.value =
                                ApiUiRequestState.Error(e.error.detail.message)

                        else ->
                            _registerState.value =
                                ApiUiRequestState.Error(e.message ?: "Unknown error")
                    }
                }
                .collect { value ->
                    _registerState.value = ApiUiRequestState.Success(value)
                }
        }
    }
}
