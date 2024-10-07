package com.vadimraspopin.mangotest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.api.errors.ValidationException
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.repository.ProfileRepository
import com.vadimraspopin.mangotest.ui.ApiUiRequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) :
    ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            repository.getUser()
                .catch { e ->
                    when (e) {
                        is ValidationException ->
                            //TODO
                            ApiUiRequestState.Error(
                                e.error.detail.map { it.msg }
                            )

                        else ->
                            //TODO
                            ApiUiRequestState.Error(listOf(e.message ?: "Unknown error"))
                    }
                }
                .collect { result ->
                    _profileState.value = ProfileState.Success(result)
                }
        }
    }

    fun refreshUser() {
        _profileState.value = ProfileState.Loading
        fetchUser()
    }
}