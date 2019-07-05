package com.nooblabs.conduit.service

import android.annotation.SuppressLint
import android.content.Context
import com.nooblabs.conduit.isNetworkAvailable
import com.nooblabs.conduit.models.Article
import com.nooblabs.conduit.models.Profile
import com.nooblabs.conduit.models.User
import com.nooblabs.conduit.service.api.UnauthorizedException
import com.nooblabs.conduit.service.api.requests.ArticleCreateRequest
import com.nooblabs.conduit.service.api.requests.LoginRequest
import com.nooblabs.conduit.service.api.requests.RegistrationRequest
import com.nooblabs.conduit.service.api.requests.UserUpdateRequest
import com.nooblabs.conduit.updateArticle
import timber.log.Timber

class Service private constructor(val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: Service? = null


        fun init(context: Context) {
            PersistenceRepository.init(context)
            instance = Service(context)
        }

        fun get() = instance!!

    }

    suspend fun getCurrentUser(): User? {
        if (isNetworkAvailable(context)) {
            Timber.d("Getting user token from db")
            val token = PersistenceRepository.getToken() ?: return null
            Timber.d("Fetching user from api")
            val userResponse = NetworkRepository.getCurrentUser(token)
            return userResponse.user
        } else {
            Timber.d("Getting user from db")
            return PersistenceRepository.getUser()
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val user = getCurrentUser()
        return user != null
    }

    suspend fun logoutUser() {
        PersistenceRepository.removeUser()
    }

    suspend fun loginUser(email: String, password: String): User {
        val loginRequest = LoginRequest(
            user = LoginRequest.User(
                email,
                password
            )
        )
        val userResponse = NetworkRepository.loginUser(loginRequest)
        saveUserToDb(userResponse.user)
        return userResponse.user
    }

    suspend fun registerUser(username: String, email: String, password: String): User {
        val registerRequest = RegistrationRequest(
            user = RegistrationRequest.User(
                username,
                email,
                password
            )
        )
        val userResponse = NetworkRepository.registerUser(registerRequest)
        saveUserToDb(userResponse.user)
        return userResponse.user

    }

    private suspend fun saveUserToDb(user: User) {
        PersistenceRepository.storeUser(user)
    }

    suspend fun updateUser(
        username: String? = null,
        email: String? = null,
        password: String? = null,
        bio: String? = null,
        image: String? = null
    ): User? {
        val updateRequest = UserUpdateRequest(
            user = UserUpdateRequest.User(
                email, username, password, image, bio
            )
        )
        val token = PersistenceRepository.getToken() ?: return null
        val userReponse = NetworkRepository.updateUser(token, updateRequest)
        saveUserToDb(userReponse.user)
        return userReponse.user
    }

    suspend fun getArticles(
        tag: String? = null,
        author: String? = null,
        favoritedBy: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): List<Article> {
        if (isNetworkAvailable(context)) {
            Timber.d("Getting new articles")
            val token = PersistenceRepository.getToken()
            return NetworkRepository.getArticles(token, tag, author, favoritedBy, limit, offset)
        }
        Timber.d("Getting old articles")
        return PersistenceRepository.getAllArticles()
    }

    suspend fun saveArticles(articles: List<Article>) {
        PersistenceRepository.saveArticles(articles)
    }

    suspend fun toggleFavoriteArticle(slug: String, favorite: Boolean): Article? {
        val token = getToken() ?: return null
        val article = NetworkRepository.toggleFavorite(token, slug, favorite)
        saveArticle(article)
        return article
    }

    suspend fun saveArticle(article: Article) {
        val storedArticles = PersistenceRepository.getAllArticles()
        val newArticles = storedArticles.updateArticle(article)
        saveArticles(newArticles)
    }

    suspend fun getToken(): String? {
        val token = PersistenceRepository.getToken() ?: return null
        return "Token $token"
    }

    suspend fun getFeed(offset: Int? = null): List<Article> {
        val token = PersistenceRepository.getToken() ?: return emptyList()
        return NetworkRepository.getFeed(token, offset)
    }

    suspend fun getArticleBySlug(slug: String): Article {
        return NetworkRepository.getArticleBySlug(slug)
    }

    suspend fun getProfile(username: String): Profile {
        val token = getToken()
        return NetworkRepository.getProfile(token, username)
    }

    suspend fun toggleFollow(username: String, follow: Boolean): Profile {
        val token = getToken() ?: throw UnauthorizedException()
        return NetworkRepository.toggleFollow(token, username, follow)
    }

    suspend fun getTags(): List<String> {
        return NetworkRepository.getTags()
    }

    suspend fun createArticle(
        title: String,
        description: String,
        body: String,
        tagList: List<String>
    ): Article {
        val token = getToken() ?: throw UnauthorizedException()
        val request = ArticleCreateRequest(
            article = ArticleCreateRequest.Article(
                title, description, body, tagList
            )
        )
        return NetworkRepository.createArticle(token, request)
    }

}