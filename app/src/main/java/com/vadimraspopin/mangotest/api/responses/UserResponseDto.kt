package com.vadimraspopin.mangotest.api.responses

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val vk: String,
    val instagram: String,
    val status: String,
    val avatar: String,
    val id: Int,
    val last: String,
    val online: Boolean,
    val created: String,
    val phone: String,
    @SerializedName("completed_task") val completedTask: String,
    val avatars: UserResponseDtoAvatars
)

data class UserResponseDtoAvatars (
    val avatar: String,
    @SerializedName("big_avatar") val bigAvatar: String,
    @SerializedName("mini_avatar") val miniAvatar: String
)