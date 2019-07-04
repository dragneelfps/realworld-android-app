package com.nooblabs.conduit.service.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.data.dao.ArticleDao
import com.nooblabs.conduit.service.data.dao.UserDao

@Database(entities = [User::class, Article::class], version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun articlesDao(): ArticleDao
}