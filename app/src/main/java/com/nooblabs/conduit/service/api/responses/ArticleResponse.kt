package com.nooblabs.conduit.service.api.responses

import com.nooblabs.conduit.models.Article
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(val article: Article)