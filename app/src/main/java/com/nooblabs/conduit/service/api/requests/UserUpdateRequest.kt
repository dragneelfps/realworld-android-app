package com.nooblabs.conduit.service.api.requests

import kotlinx.serialization.Serializable

@Serializable
class UserUpdateRequest(val user: User) {

    @Serializable
    data class User(
        val email: String? = null,
        val username: String? = null,
        val password: String? = null,
        val image: String? = null,
        val bio: String? = null
    )
}