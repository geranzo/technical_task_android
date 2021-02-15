package com.geranzo.data.repository

import com.geranzo.data.local.UsersLastPageDao
import com.geranzo.data.mapper.User2UserInDb
import com.geranzo.data.mapper.UserInDb2User
import com.geranzo.data.remote.RemoteDataSource
import com.geranzo.domain.entity.User
import com.geranzo.domain.repository.Repository
import com.geranzo.domain.repository.Result
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: UsersLastPageDao,
    private val remoteDataSource: RemoteDataSource,
    private val userInDb2User: UserInDb2User,
    private val user2UserInDb: User2UserInDb
) : Repository {

    private var lastPage = 1L

    override fun getUsers(): Observable<List<User>> {
        return dao.getAll()
            .map { list ->
                list.map(userInDb2User::map)
            }
    }

    override fun refresh(): Single<Result<Unit>> {
        return Observable.defer { remoteDataSource.getUsersPage(page = lastPage) }
            .doOnNext { result ->
                var success = false

                if (result is Result.Success) {
                    lastPage = result.data.countPages

                    if (result.data.currentPage == result.data.countPages) {
                        // we're on the last page
                        dao.dropAll()
                        dao.insertAll(result.data.users.map(user2UserInDb::map))
                        success = true
                    }
                }

                if (!success)
                    throw RuntimeException(
                        when (result) {
                            is Result.Success -> "Failed to download Users last page from the server!"
                            is Result.Error -> result.throwable.message
                            else -> "Unknown error: $result"
                        }
                    )
            }
            .retry(3)
            .onErrorReturn { throwable ->
                Result.Error(throwable)
            }
            .map { result ->
                @Suppress("UNCHECKED_CAST")
                when (result) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Error -> result
                    else -> result as Result<Unit>
                }
            }
            .single(Result.Error(RuntimeException("Received no data from the server!")))
    }
}