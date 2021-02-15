package com.geranzo.data.mapper

import com.geranzo.data.local.UserInDb
import com.geranzo.domain.entity.User

object UserInDb2User : Mapper<UserInDb, User> {
    override fun map(input: UserInDb): User = User(
        id = input.id,
        name = input.name,
        email = input.email,
        gender = input.gender,
        status = input.status,
        createdAt = input.createdAt,
        updatedAt = input.updatedAt
    )
}
