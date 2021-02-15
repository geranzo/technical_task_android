package com.geranzo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.geranzo.data.local.AppDatabase.Companion.TABLE_USERS_LAST_PAGE
import io.reactivex.Observable

@Dao
interface UsersLastPageDao {
    @Query("SELECT * FROM $TABLE_USERS_LAST_PAGE")
    fun getAll(): Observable<List<UserInDb>>

    @Insert
    fun insertAll(users: List<UserInDb>)

    @Query("DELETE FROM $TABLE_USERS_LAST_PAGE")
    fun dropAll()
}
