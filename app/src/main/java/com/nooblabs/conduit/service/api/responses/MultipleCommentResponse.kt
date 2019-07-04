package com.nooblabs.conduit.service.api.responses

import com.nooblabs.conduit.models.Comment
import kotlinx.serialization.Serializable

@Serializable
class MultipleCommentResponse(val comments: List<Comment>)