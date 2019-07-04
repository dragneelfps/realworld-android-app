package com.nooblabs.conduit.service.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class ArticleCreateRequest(val article: Article) {

    @Serializable
    data class Article(
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>? = null
    )

}