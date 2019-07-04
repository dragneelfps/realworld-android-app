package com.nooblabs.conduit.service.api

import com.nooblabs.conduit.service.api.requests.*
import com.nooblabs.conduit.service.api.responses.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface API {
    @POST("users")
    fun registerUser(@Body body: RegistrationRequest): Deferred<Response<UserResponse>>

    @POST("users/login")
    fun loginUser(@Body body: LoginRequest): Deferred<Response<UserResponse>>

    @PUT("user")
    fun updateUser(@Header("Authorization") token: String, @Body body: UserUpdateRequest): Deferred<Response<UserResponse>>

    @GET("user")
    fun getCurrentUser(@Header("Authorization") token: String): Deferred<Response<UserResponse>>

    @GET("profiles/{username}")
    fun getProfile(@Header("Authorization") token: String? = null, @Path("username") username: String): Deferred<Response<ProfileResponse>>

    @POST("profiles/{username}/follow")
    fun followUser(@Header("Authorization") token: String, @Path("username") username: String): Deferred<Response<ProfileResponse>>

    @DELETE("profiles/{username}/follow")
    fun unfollowUser(@Header("Authorization") token: String, @Path("username") username: String): Deferred<Response<ProfileResponse>>

    @GET("articles")
    fun getArticles(@Header("Authorization") token: String? = null,
                    @Query("tag") tag: String? = null,
                    @Query("author") author: String? = null,
                    @Query("favorited") favoritedBy: String? = null,
                    @Query("limit") limit: Int? = null,
                    @Query("offset") offset: Int? = null): Deferred<Response<MultipleArticleResponse>>

    @GET("articles/feed")
    fun getFeed(@Header("Authorization") token: String,
                @Query("limit") limit: Int? = null,
                @Query("offset") offset: Int? = null): Deferred<Response<MultipleArticleResponse>>

    @GET("articles/{slug}")
    fun getArticleBySlug(@Path("slug") slug: String): Deferred<Response<ArticleResponse>>

    @POST("articles/{slug}/favorite")
    fun favoriteArticle(@Header("Authorization") token: String, @Path("slug") slug: String): Deferred<Response<ArticleResponse>>

    @DELETE("articles/{slug}/favorite")
    fun unfavoriteArticle(@Header("Authorization") token: String, @Path("slug") slug: String): Deferred<Response<ArticleResponse>>

    @POST("articles")
    fun createArticle(@Header("Authorization") token: String, @Body body: ArticleCreateRequest): Deferred<Response<ArticleResponse>>

    @PUT("articles/{slug}")
    fun createArticle(@Header("Authorization") token: String, @Path("slug") slug: String, @Body body: ArticleUpdateRequest): Deferred<Response<ArticleResponse>>

    @DELETE("articles/{slug}")
    fun deleteArticle(@Header("Authorization") token: String, @Path("slug") slug: String)

    @POST("article/{slug}/comments")
    fun addComment(@Header("Authorization") token: String, @Path("slug") slug: String, @Body body: CommentCreateRequest): Deferred<Response<CommentResponse>>

    @GET("article/{slug}/comments")
    fun getComments(@Header("Authorization") token: String? = null, @Path("slug") slug: String): Deferred<Response<MultipleCommentResponse>>

    @DELETE("article/{slug}/comment/{id}")
    fun deleteComment(@Header("Authorization") token: String, @Path("slug") slug: String, @Path("id") id: String)

    @GET("tags")
    fun getTags(): Deferred<Response<TagsResponse>>

}