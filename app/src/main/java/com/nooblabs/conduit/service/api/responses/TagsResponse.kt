package com.nooblabs.conduit.service.api.responses

import kotlinx.serialization.Serializable

@Serializable
class TagsResponse(val tags: List<String>)