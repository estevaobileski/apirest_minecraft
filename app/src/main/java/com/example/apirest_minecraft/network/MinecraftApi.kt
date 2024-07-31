package com.example.apirest_minecraft.network

import com.example.apirest_minecraft.data.Item
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://minecraft-api.vercel.app/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface MinecraftApiService {
    @GET("items")
    suspend fun getItems(): List<Item>
}

object MinecraftApi {
    val retrofitService: MinecraftApiService by lazy {
        retrofit.create(MinecraftApiService::class.java)
    }
}
