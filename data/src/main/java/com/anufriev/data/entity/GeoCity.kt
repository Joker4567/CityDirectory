package com.anufriev.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoCity(
    @Json(name = "suggestions")
    val suggestions:List<Suggestions>
)

@JsonClass(generateAdapter = true)
data class Suggestions(
    @Json(name = "value")
    val value:String,
    @Json(name = "unrestricted_value")
    val unrestricted_value:String,
    @Json(name = "data")
    val data:DataCity
)

@JsonClass(generateAdapter = true)
data class DataCity(
    @Json(name = "country")
    val country:String,
    @Json(name = "city")
    val city:String
)
