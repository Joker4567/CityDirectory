package com.anufriev.data.db.entities

import java.io.Serializable

data class Organization(
    val title:String,
    val description:String,
    val rating:Int,
    val phone:String,
    val feedback:List<FeedBack>
):Serializable
