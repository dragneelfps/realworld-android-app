package com.nooblabs.conduit.service.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class CommentCreateRequest(val comment: Comment) {
    @Serializable
    data class Comment(val body: String)
}