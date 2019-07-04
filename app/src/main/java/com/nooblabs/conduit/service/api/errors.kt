package com.nooblabs.conduit.service.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

sealed class ApiException : RuntimeException()

@Serializable
data class ValidationException(val errors: Map<String, List<String>>) : ApiException() {
    var statusCode: Int = -1
}

class UnauthorizedException : ApiException()

class ForbiddenException : ApiException()

class NotFoundException : ApiException()

class GenericException(val statusCode: Int): ApiException()

class TokenNotFound : RuntimeException()


private val json = Json(JsonConfiguration.Stable)

fun getAptError(statusCode: Int, jsonString: String?): ApiException {
    return when (statusCode) {
        422 -> {
            json.parse(
                ValidationException.serializer(),
                jsonString!!
            ).apply { this.statusCode = statusCode }
        }
        401 -> {
            UnauthorizedException()
        }
        403 -> {
            ForbiddenException()
        }
        404 -> {
            NotFoundException()
        }
        else -> {
            GenericException(statusCode)
        }
    }
}