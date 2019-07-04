package com.nooblabs.conduit.service.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nooblabs.conduit.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User

    @Insert
    suspend fun insert(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    @Query("SELECT token FROM user LIMIT 1")
    suspend fun getToken(): String

}