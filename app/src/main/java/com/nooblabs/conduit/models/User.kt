package com.nooblabs.conduit.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class User(
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: String?
) {
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}