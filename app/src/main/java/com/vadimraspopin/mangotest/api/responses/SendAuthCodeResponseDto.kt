package com.vadimraspopin.mangotest.api.responses

import com.google.gson.annotations.SerializedName

data class SendAuthCodeResponseDto(
    @SerializedName("is_success") val isSuccess: Boolean
)
