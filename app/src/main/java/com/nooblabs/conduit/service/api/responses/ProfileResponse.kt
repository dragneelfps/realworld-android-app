package com.nooblabs.conduit.service.api.responses

import com.nooblabs.conduit.models.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(val profile: Profile)