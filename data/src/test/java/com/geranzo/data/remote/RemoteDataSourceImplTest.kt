package com.geranzo.data.remote

import com.geranzo.domain.entity.User
import com.geranzo.domain.repository.Result
import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RemoteDataSourceImplTest {

    companion object {
        const val USER_ID = 1L
        const val USER_NAME = "John Doe"
        const val USER_EMAIL = "john.doe@domain.com"
        const val USER_GENDER = "Male"
        const val USER_STATUS = "Active"
        const val USER_CREATED_AT = "2021-02-15T08:50:05.751+01:00"
        const val USER_CREATED_AT_TIMESTAMP = 1613375405751L    // https://dencode.com/date/unix-time
        const val USER_UPDATED_AT = "2021-02-15T13:50:06.099+05:30"
        const val USER_UPDATED_AT_TIMESTAMP = 1613377206099L

        const val COUNT_USERS = 105L
        const val COUNT_PAGES = 6L
        const val CURRENT_PAGE = 1L
        const val COUNT_USERS_PER_PAGE = 20L

        const val EXCEPTION_MESSAGE = "exception happened"
    }

    private val user = User(
        id = USER_ID,
        name = USER_NAME,
        email = USER_EMAIL,
        gender = USER_GENDER,
        status = USER_STATUS,
        createdAt = USER_CREATED_AT_TIMESTAMP,
        updatedAt = USER_UPDATED_AT_TIMESTAMP,
    )

    private val userDTO = GoRestModel.UserDTO(
        id = USER_ID,
        name = USER_NAME,
        email = USER_EMAIL,
        gender = USER_GENDER,
        status = USER_STATUS,
        createdAt = USER_CREATED_AT,
        updatedAt = USER_UPDATED_AT,
    )

    private val goRestModel = GoRestModel(
        responseCode = 200L,
        metaData = GoRestModel.Meta(
            pagination = GoRestModel.Pagination(
                countUsers = COUNT_USERS,
                countPages = COUNT_PAGES,
                currentPage = CURRENT_PAGE,
                countUsersPerPage = COUNT_USERS_PER_PAGE,
            )
        ),
        users = listOf(userDTO)
    )

    private lateinit var api: GoRestApi
    private lateinit var remote: RemoteDataSourceImpl


    @Before
    fun setUp() {
        api = mock(GoRestApi::class.java)
        remote = RemoteDataSourceImpl(api)
    }

    @Test
    fun getUsersPage_ApiGivesCorrectResponse_returnsUsers() {
        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(goRestModel))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        result as Result.Success
        assertThat(result.data.countPages).isEqualTo(COUNT_PAGES)
        assertThat(result.data.currentPage).isEqualTo(CURRENT_PAGE)
        assertThat(result.data.users[0]).isEqualTo(user)
    }

    @Test
    fun getUsersPage_ApiThrowsException_returnsErrorWithThrowable() {
        `when`(api.getUsers(anyLong()))
            .thenThrow(RuntimeException(EXCEPTION_MESSAGE))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        result as Result.Error
        assertThat(result.throwable.message).isEqualTo(EXCEPTION_MESSAGE)
    }

    @Test
    fun getUsersPage_ApiGivesStatusCodeNot200_returnsErrorWithThrowable() {
        val model = goRestModel.copy(responseCode = 500)

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiResponseMissesStatusCode_returnsErrorWithThrowable() {
        val model = goRestModel.copy(responseCode = null)

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiResponseMissesPagination_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            metaData = goRestModel.metaData!!.copy(pagination = null)
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiResponseMissesMetaData_returnsErrorWithThrowable() {
        val model = goRestModel.copy(metaData = null)

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiResponseMissesUsers_returnsErrorWithThrowable() {
        val model = goRestModel.copy(users = null)

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiResponseWithEmptyUsers_returnsEmptyUsers() {
        val model = goRestModel.copy(users = listOf())

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        result as Result.Success
        assertThat(result.data.users.isEmpty()).isTrue()
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingId_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(id = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingName_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(name = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingEmail_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(email = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingGender_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(gender = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingStatus_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(status = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingCreatedAt_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(createdAt = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithMissingUpdatedAt_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(updatedAt = null))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithInvalidCreatedAt_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(createdAt = "invalid date time"))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getUsersPage_ApiGivesUserWithInvalidUpdatedAt_returnsErrorWithThrowable() {
        val model = goRestModel.copy(
            users = listOf(userDTO.copy(updatedAt = "invalid date time"))
        )

        `when`(api.getUsers(anyLong()))
            .thenReturn(Observable.just(model))

        val observable = remote.getUsersPage()
        val result = observable.blockingSingle()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}
