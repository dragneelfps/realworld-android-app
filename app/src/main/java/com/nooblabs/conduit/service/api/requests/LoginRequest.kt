package com.nooblabs.conduit.service.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val user: User) {
    @Serializable
    data class User(
        val email: String,
        val password: String
    )
}