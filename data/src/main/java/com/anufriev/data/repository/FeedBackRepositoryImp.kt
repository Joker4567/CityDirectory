package com.anufriev.data.repository

import com.anufriev.data.db.dao.FeedBackDao
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.entity.FeedBack
import com.anufriev.data.network.ApiService
import com.anufriev.utils.platform.BaseRepository
import com.anufriev.utils.platform.ErrorHandler
import com.anufriev.utils.platform.State
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FeedBackRepositoryImp(
    errorHandler: ErrorHandler,
    private val api: ApiService,
    private val feedBackDao: FeedBackDao
    ) : BaseRepository(errorHandler), FeedBackRepository {

    override suspend fun getFeedBackList(id: Int): List<FeedBackDaoEntity> = feedBackDao.getFeedBackList(id)
    override suspend fun setFeedBackList(list: List<FeedBack>, idOrg:Int) {
        feedBackDao.setFeedBackList(list.map { it.from(idOrg) })
    }

    override suspend fun getFeedBackListNetwork(
        idOrg:Int,
        onSuccess: (List<FeedBack>) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            api.getReviews(idOrg)
        }
    }

    override suspend fun setRatingReviews(
        flag:Boolean,
        text:String,
        idOrg: Int,
        onSuccess: (FeedBack) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState){
            val jsonObject = JSONObject()
            jsonObject.put("isPositive", flag)
            jsonObject.put("text", text)
            val jsonObjectString = jsonObject.toString()
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            api.setRatingReviews(idOrg,requestBody)
        }
    }

}