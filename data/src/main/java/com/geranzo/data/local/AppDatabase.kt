package com.geranzo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserInDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersLastPageDao(): UsersLastPageDao


    companion object {
        private const val DB_NAME = "SliideTest.db"

        const val TABLE_USERS_LAST_PAGE = "UsersLastPage"
        const val COLUMN_USERS_LAST_PAGE_ID = "id"
        const val COLUMN_USERS_LAST_PAGE_NAME = "name"
        const val COLUMN_USERS_LAST_PAGE_EMAIL = "email"
        const val COLUMN_USERS_LAST_PAGE_GENDER = "gender"
        const val COLUMN_USERS_LAST_PAGE_STATUS = "status"
        const val COLUMN_USERS_LAST_PAGE_CREATED = "created"
        const val COLUMN_USERS_LAST_PAGE_UPDATED = "updated"

        fun create(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, DB_NAME
        )
            .build()
    }
}
