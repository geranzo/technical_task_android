package com.geranzo.data.remote

import com.geranzo.domain.entity.User

data class UsersPage(
    val currentPage: Long,
    val countPages: Long,
    val users: List<User>,
)
