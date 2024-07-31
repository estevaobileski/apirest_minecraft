package com.example.apirest_minecraft.data

import com.squareup.moshi.Json

data class Item(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "image") val image: String
)
data class ApiResponse(
    val items: List<Item>
)