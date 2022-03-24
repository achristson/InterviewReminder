package com.achristson.interviewreminder.network

import com.achristson.interviewreminder.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface LogoApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: Bearer ${Constants.API_KEY}",
        "cache-control: no-cache"
    )
    @POST("api/categorize")
    suspend fun getLogo(
        @Query("url") url: String
    ) : Any
}

object LogoApi {
    val retrofitService : LogoApiService by lazy {
        retrofit.create(LogoApiService::class.java)
    }
}