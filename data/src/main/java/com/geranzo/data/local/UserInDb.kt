package com.geranzo.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_CREATED
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_EMAIL
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_GENDER
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_ID
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_NAME
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_STATUS
import com.geranzo.data.local.AppDatabase.Companion.COLUMN_USERS_LAST_PAGE_UPDATED
import com.geranzo.data.local.AppDatabase.Companion.TABLE_USERS_LAST_PAGE

@Entity(
    tableName = TABLE_USERS_LAST_PAGE,
    indices = [
        Index(value = [COLUMN_USERS_LAST_PAGE_ID]),
    ]
)
data class UserInDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_ID)
    val id: Long,
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_EMAIL)
    val email: String,
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_GENDER)
    val gender: String,
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_STATUS)
    val status: String,
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_CREATED)
    val createdAt: Long,
    @ColumnInfo(name = COLUMN_USERS_LAST_PAGE_UPDATED)
    val updatedAt: Long,
)
