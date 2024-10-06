package com.vadimraspopin.mangotest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            repository.fetchUser().collect { result ->
                result
                    .onSuccess { user ->
                        _profileState.value = ProfileState.Success(user)
                    }
                    .onFailure { throwable ->
                        _profileState.value = ProfileState.Error(throwable.localizedMessage
                            ?: "Unknown Error")
                    }
            }
        }
    }

    fun refreshUser() {
        _profileState.value = ProfileState.Loading
        fetchUser()
    }
}