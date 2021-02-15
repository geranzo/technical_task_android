package com.geranzo.domain.entity

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val gender: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
)