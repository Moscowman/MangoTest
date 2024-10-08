package com.vadimraspopin.mangotest.api.requests

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    val name: String,
    val username: String,
    val birthday: String? = null,
    val city: String = "",
    val vk: String = "",
    val instagram: String = "",
    val status: String = "",
    val avatar: AvatarData? = null
)

data class AvatarData(
    val filename: String,
    @SerializedName("base_64") val data: String
)