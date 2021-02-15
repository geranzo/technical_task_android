package com.geranzo.data.mapper

import com.geranzo.data.local.UserInDb
import com.geranzo.domain.entity.User

object User2UserInDb : Mapper<User, UserInDb> {
    override fun map(input: User): UserInDb = UserInDb(
        id = input.id,
        name = input.name,
        email = input.email,
        gender = input.gender,
        status = input.status,
        createdAt = input.createdAt,
        updatedAt = input.updatedAt
    )
}
