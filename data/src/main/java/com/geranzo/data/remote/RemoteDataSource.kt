package com.geranzo.data.remote

import com.geranzo.domain.repository.Result
import io.reactivex.Observable

interface RemoteDataSource {
    fun getUsersPage(page: Long = 1L): Observable<Result<UsersPage>>
}
