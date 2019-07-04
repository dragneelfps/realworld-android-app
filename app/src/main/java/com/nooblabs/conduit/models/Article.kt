package com.nooblabs.conduit.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class Article(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    @Embedded val author: Author
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
    @Serializable
    data class Author(
        val username: String,
        val bio: String?,
        val image: String?,
        val following: Boolean
    )
}