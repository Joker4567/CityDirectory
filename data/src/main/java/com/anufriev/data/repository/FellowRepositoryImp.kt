package com.anufriev.data.repository

import com.anufriev.data.db.dao.FellowDao
import com.anufriev.data.db.entities.FellowDaoEntity
import com.anufriev.data.entity.Fellow
import com.anufriev.data.network.ApiService
import com.anufriev.utils.platform.BaseRepository
import com.anufriev.utils.platform.ErrorHandler
import com.anufriev.utils.platform.State
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FellowRepositoryImp(
    errorHandler: ErrorHandler,
    private val api:ApiService,
    private val repository:FellowDao
) : BaseRepository(errorHandler = errorHandler),FellowRepository {
    override suspend fun getFellowListNetwork(
        city: String,
        onSuccess: (List<Fellow>) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            val response = api.getFellows(city)
            var list = emptyList<Fellow>().toMutableList()
            if(response.isSuccessful){
                list = response.body()?.toMutableList() ?: emptyList<Fellow>().toMutableList()
            } else {
                if(response.code() == 204) emptyList<Fellow>()
            }
            list
        }
    }

    override suspend fun setFellow(
        city: String,
        date: String,
        description: String,
        onSuccess: (Fellow) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            val jsonObject = JSONObject()
            jsonObject.put("city", city)
            jsonObject.put("date", date)
            jsonObject.put("description", description)
            val jsonObjectString = jsonObject.toString()
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            api.setFellow(city, requestBody)
        }
    }

    override suspend fun getFellowList(city: String): List<FellowDaoEntity> = repository.getFellowList(city)

    override suspend fun setFellowList(list: List<Fellow>) = repository.setFellowList(list.map { it.from() })
    override suspend fun deleteAllFellows() = repository.deleteAllFellows()

}