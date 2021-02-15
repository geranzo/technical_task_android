package com.geranzo.data.repository

import com.geranzo.data.local.UsersLastPageDao
import com.geranzo.data.mapper.User2UserInDb
import com.geranzo.data.mapper.UserInDb2User
import com.geranzo.data.remote.RemoteDataSource
import com.geranzo.data.remote.UsersPage
import com.geranzo.domain.repository.Result
import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*

class RepositoryImplTest {

    private lateinit var dao: UsersLastPageDao
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: RepositoryImpl


    @Before
    fun setUp() {
        dao = mock(UsersLastPageDao::class.java)
        doNothing().`when`(dao).insertAll(anyList())

        remoteDataSource = mock(RemoteDataSource::class.java)
        repository = RepositoryImpl(dao, remoteDataSource, UserInDb2User, User2UserInDb)
    }

    @Test
    fun refresh_remoteDataSourceGivesLastPage_returnsSuccess() {
        `when`(remoteDataSource.getUsersPage(anyLong()))
            .thenReturn(Observable.just(Result.Success(
                UsersPage(
                    currentPage = 5L,
                    countPages = 5L,
                    emptyList()
                )
            )))

        val result = repository.refresh().blockingGet()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        verify(remoteDataSource, times(1)).getUsersPage(anyLong())
        verify(dao, times(1)).insertAll(anyList())
    }

    @Test
    fun refresh_remoteDataSourceGivesFirstThenLastPage_returnsSuccess() {
        `when`(remoteDataSource.getUsersPage(1L))
            .thenReturn(Observable.just(Result.Success(
                UsersPage(
                    currentPage = 1L,
                    countPages = 7L,
                    emptyList()
                )
            )))
        `when`(remoteDataSource.getUsersPage(7L))
            .thenReturn(Observable.just(Result.Success(
                UsersPage(
                    currentPage = 7L,
                    countPages = 7L,
                    emptyList()
                )
            )))

        val result = repository.refresh().blockingGet()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        verify(remoteDataSource, times(2)).getUsersPage(anyLong())
        verify(dao, times(1)).insertAll(anyList())
    }

    @Test
    fun refresh_remoteDataSourceNeverGivesLastPage_returnsError() {
        `when`(remoteDataSource.getUsersPage(anyLong()))
            .thenReturn(Observable.just(Result.Success(
                UsersPage(
                    currentPage = 8L,
                    countPages = 9L,
                    emptyList()
                )
            )))

        val result = repository.refresh().blockingGet()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        verify(remoteDataSource, times(4)).getUsersPage(anyLong())
        inOrder(dao).verify(dao, never()).insertAll(anyList())
    }
}
