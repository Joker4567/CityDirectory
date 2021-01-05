package com.anufriev.data.entity

import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Organization(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,
    @Json(name = "phoneNumber")
    val phoneNumber:String,
    @Json(name = "rating")
    val rating:Int,
    @Json(name = "description")
    val description:String,
    @Json(name = "badPositive")
    val badPositive:String
) {
    fun from() = OrganizationDaoEntity(
        id, name, phoneNumber.trim(), rating, description, badPositive
    )
}
