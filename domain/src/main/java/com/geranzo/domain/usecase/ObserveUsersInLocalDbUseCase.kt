package com.geranzo.domain.usecase

import com.geranzo.domain.repository.Repository
import javax.inject.Inject

class ObserveUsersInLocalDbUseCase @Inject constructor(private val repository: Repository) {
    fun get() = repository.getUsers()
}
