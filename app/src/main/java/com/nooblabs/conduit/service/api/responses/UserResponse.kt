package com.nooblabs.conduit.service.api.responses

import com.nooblabs.conduit.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val user: User)