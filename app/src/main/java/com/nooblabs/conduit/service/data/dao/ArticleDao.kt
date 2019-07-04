package com.nooblabs.conduit.service.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nooblabs.conduit.models.Article

@Dao
interface ArticleDao {

    @Insert
    suspend fun insert(articles: List<Article>)

    @Query("SELECT * FROM article")
    suspend fun getAllArticles(): List<Article>

    @Query("DELETE FROM article")
    suspend fun deleteAllArticles()



}