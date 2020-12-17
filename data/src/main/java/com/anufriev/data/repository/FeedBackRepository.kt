package com.anufriev.data.repository

import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.entity.FeedBack
import com.anufriev.data.entity.Organization
import com.anufriev.utils.platform.State

interface FeedBackRepository {
    suspend fun getFeedBackList(id:Int):List<FeedBackDaoEntity>
    suspend fun setFeedBackList(list:List<FeedBack>, idOrg:Int)
    suspend fun getFeedBackListNetwork(
        idOrg:Int,
        onSuccess: (List<FeedBack>) -> Unit,
        onState: (State) -> Unit
    )
    suspend fun setRatingReviews(
        flag:Boolean,
        text:String,
        idOrg:Int,
        onSuccess: (FeedBack) -> Unit,
        onState: (State) -> Unit
    )
}