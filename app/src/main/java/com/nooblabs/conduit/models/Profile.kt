package com.nooblabs.conduit.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Profile(
    val username: String,
    val bio: String?,
    val image: String?,
    val following: Boolean
) {

    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}