package com.vadimraspopin.mangotest.api

import com.google.gson.annotations.SerializedName

data class GetMyProfileRequest(
    @SerializedName("access_token") val accessToken: String
)
