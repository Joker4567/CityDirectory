package com.anufriev.data.entity

import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedBack(
    val id:Int,
    val isPositive:Boolean,
    val text:String
) {
    fun from(idOrg:Int, uid:String) = FeedBackDaoEntity(
        id, idOrg, isPositive, text, 0, uid
    )
}
