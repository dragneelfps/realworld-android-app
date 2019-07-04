package com.nooblabs.conduit.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val body: String,
    val author: Author
) {

    @Serializable
    data class Author(
        val username: String,
        val bio: String? = null,
        val image: String? = null,
        val following: Boolean = false
    )
}