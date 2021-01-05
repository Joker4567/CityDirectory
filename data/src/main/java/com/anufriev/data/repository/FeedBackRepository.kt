package com.anufriev.data.repository

import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.entity.FeedBack
import com.anufriev.data.entity.Organization
import com.anufriev.utils.platform.State

interface FeedBackRepository {
    suspend fun getFeedBackList(id:Int):List<FeedBackDaoEntity>
    suspend fun setFeedBackList(list:List<FeedBack>, idOrg:Int, uid:String)
    suspend fun getFeedBackListNetwork(
        idOrg:Int,
        onSuccess: (List<FeedBack>) -> Unit,
        onState: (State) -> Unit
    )
    suspend fun setRatingReviews(
        flag:Boolean,
        text:String,
        date:String,
        idOrg:Int,
        onSuccess: (FeedBack) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getLastFeedBack(id:Int, uid:String):FeedBackDaoEntity

    suspend  fun updateFeedBack(item:FeedBackDaoEntity)
}