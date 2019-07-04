package com.nooblabs.conduit.service.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.serializer

class CustomTypeConverters {

    private val json = Json(JsonConfiguration.Stable)

    @TypeConverter
    fun fromJsonToListString(input: String): List<String> {
        return json.parse(String.serializer().list, input)
    }

    @TypeConverter
    fun fromListStringToJson(input: List<String>): String {
        return json.stringify(String.serializer().list, input)
    }

}