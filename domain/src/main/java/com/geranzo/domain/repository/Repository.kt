package com.geranzo.domain.repository

import com.geranzo.domain.entity.User
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
    // refresh local data with remote one
    fun refresh(): Single<Result<Unit>>

    // get local data
    fun getUsers(): Observable<List<User>>
}