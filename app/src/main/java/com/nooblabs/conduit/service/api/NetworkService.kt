package com.nooblabs.conduit.service.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber

@UseExperimental(UnstableDefault::class)
private val retrofit = Retrofit.Builder()
    .client(
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.tag("OkHttp").d(message)
                    }
                }
            ).apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    )
    .baseUrl("https://conduit.productionready.io/api/")
    .addConverterFactory(
        Json(
            configuration = JsonConfiguration(
                encodeDefaults = false,
                strictMode = false,
                prettyPrint = true
            )
        ).asConverterFactory("application/json".toMediaType())
    )
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

val networkService: API = retrofit.create(API::class.java)