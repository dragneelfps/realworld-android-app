package com.nooblabs.conduit.service.api.responses

import com.nooblabs.conduit.models.Comment
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(val comment: Comment)