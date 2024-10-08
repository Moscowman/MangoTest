package com.vadimraspopin.mangotest.api.responses

data class ProfileUpdateResponseDto(
    val avatars: ProfileUpdateResponseDtoAvatars?
)

data class ProfileUpdateResponseDtoAvatars (
    val avatar: String?,
    val bigAvatar: String?,
    val miniAvatar: String?
)