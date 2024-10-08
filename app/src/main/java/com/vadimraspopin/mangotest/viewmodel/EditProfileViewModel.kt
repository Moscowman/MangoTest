package com.vadimraspopin.mangotest.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadimraspopin.mangotest.api.requests.AvatarData
import com.vadimraspopin.mangotest.api.requests.ProfileUpdateRequest
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.repository.ProfileRepository
import com.vadimraspopin.mangotest.ui.ApiUiRequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    val currentUser = profileRepository.getCachedUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _updateProfileState =
        MutableStateFlow<ApiUiRequestState<User>>(ApiUiRequestState.Idle)
    val updateProfileState: StateFlow<ApiUiRequestState<User>> = _updateProfileState.asStateFlow()

    fun updateProfile(
        name: String,
        username: String,
        city: String = "",
        birthday: String? = null,
        aboutMe: String = "",
        avatarUri: Uri?,
        context: Context
    ) {

        viewModelScope.launch {
            val avatarData = withContext(Dispatchers.IO) {
                avatarUri?.let { uri ->
                    val contentResolver = context.contentResolver

                    val fileName = getFileName(contentResolver, uri) ?: "avatar"

                    val inputStream = contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()

                    val base64File = bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }

                    if (base64File != null) {
                        AvatarData(filename = fileName, data = base64File)
                    } else {
                        null
                    }
                }
            }

            val profileUpdateRequest = ProfileUpdateRequest(
                name = name,
                username = username,
                city = city,
                birthday = birthday,
                status = aboutMe,
                avatar = avatarData
            )

            profileRepository.updateProfile(profileUpdateRequest)
                .onStart {
                    _updateProfileState.value = ApiUiRequestState.Loading
                }
                .catch { exception ->
                    _updateProfileState.value = ApiUiRequestState.Error(
                        listOf(exception.message ?: "Произошла ошибка")
                    )
                }
                .collect { user ->
                    _updateProfileState.value = ApiUiRequestState.Success(user)
                }
        }
    }

    private fun getFileName(contentResolver: android.content.ContentResolver, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = it.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }
}
