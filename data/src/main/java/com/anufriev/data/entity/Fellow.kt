package com.anufriev.data.entity

import com.anufriev.data.db.entities.FellowDaoEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fellow(
    @Json(name = "id")
    val id:Int,
    @Json(name = "city")
    val city:String = "",
    @Json(name = "description")
    val description:String = "",
    @Json(name = "date")
    val date:String = "",
    @Json(name = "phone")
    val phone:String = ""
) {
    fun from() = FellowDaoEntity(
        id, city, description, date, phone
    )
}
