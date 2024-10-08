package com.vadimraspopin.mangotest.api.mappers

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