package com.vadimraspopin.mangotest.api.mappers

import com.vadimraspopin.mangotest.api.responses.ProfileUpdateResponseDto
import com.vadimraspopin.mangotest.api.responses.UserResponseDto
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.model.UserAvatars

fun UserResponseDto.toDomainModel(): User = with(profileData) {
    User(
        name = this.name,
        username = this.username,
        birthday = this.birthday,
        city = this.city,
        vk = this.vk,
        instagram = this.instagram,
        status = this.status,
        avatar = this.avatar,
        id = this.id,
        last = this.last,
        online = this.online,
        created = this.created,
        phone = this.phone,
        completedTask = this.completedTask,
        avatars = avatars?.let {
            UserAvatars(
                avatar = it.avatar,
                bigAvatar = it.bigAvatar,
                miniAvatar = it.miniAvatar
            )
        }
    )
}

fun ProfileUpdateResponseDto.toDomainModel(): User =
    User(
        name = "",
        username = "",
        birthday = "",
        city = "",
        vk = "",
        instagram = "",
        status = "",
        avatar = "",
        id = -1,
        last = "",
        online = false,
        created = "",
        phone = "",
        completedTask = "",
        avatars = avatars?.let { avatars ->
            UserAvatars(
                avatar = avatars.avatar,
                bigAvatar = avatars.bigAvatar,
                miniAvatar = avatars.miniAvatar
            )
        }
    )
