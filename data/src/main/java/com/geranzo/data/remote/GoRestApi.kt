package com.geranzo.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GoRestApi {

    companion object {
        const val BASE_URL = "https://gorest.co.in/public-api/"
    }

    /**
     * example: https://gorest.co.in/public-api/users
     */
    @GET("users")
    fun getUsers(
        @Query("page") page: Long
    ): Observable<GoRestModel>
}
