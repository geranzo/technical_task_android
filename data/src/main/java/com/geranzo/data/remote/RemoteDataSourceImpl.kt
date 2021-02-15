package com.geranzo.data.remote

import com.geranzo.data.utils.DateTimeUtils
import com.geranzo.domain.entity.User
import com.geranzo.domain.repository.Result
import io.reactivex.Observable
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val api: GoRestApi) : RemoteDataSource {
    override fun getUsersPage(page: Long): Observable<Result<UsersPage>> {
        return try {
            api.getUsers(page)
                .map { model ->
                    try {
                        if (model.responseCode!! != 200L)
                            throw IllegalStateException("Expected 200 but response code: ${model.responseCode}")

                        Result.Success(
                            UsersPage(
                                currentPage = model.metaData!!.pagination!!.currentPage!!,
                                countPages = model.metaData.pagination!!.countPages!!,
                                users = model.users!!.map { userDTO ->
                                    User(
                                        id = userDTO.id!!,
                                        name = userDTO.name!!,
                                        email = userDTO.email!!,
                                        gender = userDTO.gender!!,
                                        status = userDTO.status!!,
                                        createdAt = DateTimeUtils.parseIso8601DateTime(userDTO.createdAt!!)!!.time,
                                        updatedAt = DateTimeUtils.parseIso8601DateTime(userDTO.updatedAt!!)!!.time
                                    )
                                }
                            )
                        )
                    } catch (ex: Exception) {
                        Result.Error(ex)
                    }
                }.onErrorReturn {
                    Result.Error(it)
                }
        } catch (th: Throwable) {
            Observable.just(Result.Error(th))
        }
    }
}
