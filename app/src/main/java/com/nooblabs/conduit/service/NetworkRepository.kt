package com.nooblabs.conduit.service

import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.models.Profile
import com.nooblabs.conduit.service.api.getAptError
import com.nooblabs.conduit.service.api.networkService
import com.nooblabs.conduit.service.api.requests.ArticleCreateRequest
import com.nooblabs.conduit.service.api.requests.LoginRequest
import com.nooblabs.conduit.service.api.requests.RegistrationRequest
import com.nooblabs.conduit.service.api.requests.UserUpdateRequest
import com.nooblabs.conduit.service.api.responses.UserResponse

object NetworkRepository {

    suspend fun registerUser(request: RegistrationRequest): UserResponse {
        val response = networkService.registerUser(request).await()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())

        }
    }

    suspend fun loginUser(request: LoginRequest): UserResponse {
        val response = networkService.loginUser(request).await()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun getCurrentUser(token: String): UserResponse {
        val response = networkService.getCurrentUser("Token $token").await()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun updateUser(token: String, updateRequest: UserUpdateRequest): UserResponse {
        val response = networkService.updateUser("Token $token", updateRequest).await()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun getArticles(
        token: String? = null,
        tag: String? = null,
        author: String? = null,
        favoritedBy: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): List<Article> {
        val authHeader = if (token != null) "Token $token" else null
        val response =
            networkService.getArticles(
                token = authHeader,
                tag = tag,
                author = author,
                favoritedBy = favoritedBy,
                limit = limit,
                offset = offset
            ).await()
        if (response.isSuccessful) {
            return response.body()!!.articles
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun toggleFavorite(token: String, slug: String, favorite: Boolean): Article {
        val response =
            if (favorite)
                networkService.favoriteArticle(token, slug).await()
            else
                networkService.unfavoriteArticle(token, slug).await()

        if (response.isSuccessful) {
            return response.body()!!.article
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun getFeed(token: String, offset: Int? = null): List<Article> {
        val authHeader = "Token $token"
        val response = networkService.getFeed(authHeader, offset = offset).await()
        if (response.isSuccessful) {
            return response.body()!!.articles
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun getArticleBySlug(slug: String): Article {
        val response = networkService.getArticleBySlug(slug).await()
        if (response.isSuccessful) {
            return response.body()!!.article
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun getProfile(token: String?, username: String): Profile {
        val response = networkService.getProfile(token, username).await()
        if (response.isSuccessful) {
            return response.body()!!.profile
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun toggleFollow(token: String, username: String, follow: Boolean): Profile {
        val response =
            if(follow)
                networkService.followUser(token, username).await()
            else
                networkService.unfollowUser(token, username).await()
        if (response.isSuccessful) {
            return response.body()!!.profile
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun getTags(): List<String> {
        val response = networkService.getTags().await()
        if (response.isSuccessful) {
            return response.body()!!.tags
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }

    suspend fun createArticle(token: String, request: ArticleCreateRequest): Article {
        val response = networkService.createArticle(token, request).await()
        if (response.isSuccessful) {
            return response.body()!!.article
        } else {
            throw getAptError(response.code(), response.errorBody()?.string())
        }
    }


}