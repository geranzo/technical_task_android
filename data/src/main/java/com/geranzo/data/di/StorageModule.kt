package com.geranzo.data.di

import android.content.Context
import com.geranzo.data.local.AppDatabase
import com.geranzo.data.local.UsersLastPageDao
import com.geranzo.data.mapper.User2UserInDb
import com.geranzo.data.mapper.UserInDb2User
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.create(context)

    @Provides
    @Singleton
    fun provideUsersLastPageDao(appDatabase: AppDatabase): UsersLastPageDao = appDatabase.usersLastPageDao()

    @Provides
    @Singleton
    fun provideUserInDb2User(): UserInDb2User = UserInDb2User

    @Provides
    @Singleton
    fun provideUser2UserInDb(): User2UserInDb = User2UserInDb
}
