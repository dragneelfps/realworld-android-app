package com.nooblabs.conduit.service.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(val user: User) {
    @Serializable
    data class User(
        val username: String,
        val email: String,
        val password: String
    )
}