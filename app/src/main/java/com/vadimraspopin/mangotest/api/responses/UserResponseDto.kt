package com.vadimraspopin.mangotest.api.responses

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("profile_data") val profileData: UserResponseProfileData
)

data class UserResponseProfileData(
    val name: String,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: String?,
    val id: Int,
    val last: String?,
    val online: Boolean,
    val created: String?,
    val phone: String?,
    @SerializedName("completed_task") val completedTask: String?,
    val avatars: UserResponseDtoAvatars?
)

data class UserResponseDtoAvatars (
    val avatar: String?,
    val bigAvatar: String?,
    val miniAvatar: String?
)