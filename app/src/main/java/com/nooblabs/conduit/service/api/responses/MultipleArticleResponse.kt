package com.nooblabs.conduit.service.api.responses

import com.nooblabs.conduit.models.Article
import kotlinx.serialization.Serializable

@Serializable
data class MultipleArticleResponse(
    val articles: List<Article>
)