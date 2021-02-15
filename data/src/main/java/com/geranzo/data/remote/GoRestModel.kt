package com.geranzo.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoRestModel(
    @Json(name = "code")
    val responseCode: Long? = null,
    @Json(name = "meta")
    val metaData: Meta? = null,
    @Json(name = "data")
    val users: List<UserDTO>? = null,
) {
    @JsonClass(generateAdapter = true)
    data class Meta(
        val pagination: Pagination? = null,
    )

    @JsonClass(generateAdapter = true)
    data class Pagination(
        @Json(name = "total")
        val countUsers: Long? = null,
        @Json(name = "pages")
        val countPages: Long? = null,
        @Json(name = "page")
        val currentPage: Long? = null,
        @Json(name = "limit")
        val countUsersPerPage: Long? = null,
    )

    @JsonClass(generateAdapter = true)
    data class UserDTO(
        val id: Long? = null,
        val name: String? = null,
        val email: String? = null,
        val gender: String? = null,
        val status: String? = null,
        @Json(name = "created_at")
        val createdAt: String? = null,
        @Json(name = "updated_at")
        val updatedAt: String? = null,
    )
}

/*
{
    "code": 200,
    "meta": {
        "pagination": {
            "total": 1454,
            "pages": 73,
            "page": 1,
            "limit": 20
        }
    },
    "data": [
        {
            "id": 81,
            "name": "Eshana Mehra V",
            "email": "v_mehra_eshana@barrows-goldner.co",
            "gender": "Female",
            "status": "Active",
            "created_at": "2021-02-12T03:50:05.751+05:30",
            "updated_at": "2021-02-12T03:50:05.751+05:30"
        },
    ...
    ]
}
*/
