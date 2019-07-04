package com.nooblabs.conduit.service.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class ArticleUpdateRequest(val article: Article) {

    @Serializable
    data class Article(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null
    )

}