package com.geranzo.domain.usecase

import com.geranzo.domain.repository.Repository
import com.geranzo.domain.repository.Result
import io.reactivex.Observable
import javax.inject.Inject

class RefreshUsersInLocalDbWithRemoteOnesUseCase @Inject constructor(private val repository: Repository) {
    fun refresh() =
        Observable.defer {
            repository.refresh().toObservable()
        }.startWith(Result.Loading)
}
