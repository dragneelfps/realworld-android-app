package com.nooblabs.conduit.service

import android.content.Context
import androidx.room.Room
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.data.AppDatabase

object PersistenceRepository {

    private lateinit var db: AppDatabase

    fun init(context: Context) {
        if (!::db.isInitialized) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "conduit-db").build()
        }
    }

    suspend fun storeUser(user: User) {
        removeUser()
        db.userDao().insert(user)
    }

    suspend fun getUser(): User? {
        return db.userDao().getUser()
    }

    suspend fun getToken(): String? {
        return db.userDao().getToken()
    }

    suspend fun removeUser() {
        db.userDao().deleteUser()
    }

    suspend fun saveArticles(articles: List<Article>) {
        db.articlesDao().deleteAllArticles()
        db.articlesDao().insert(articles)
    }

    suspend fun getAllArticles(): List<Article> {
        return db.articlesDao().getAllArticles()
    }

}