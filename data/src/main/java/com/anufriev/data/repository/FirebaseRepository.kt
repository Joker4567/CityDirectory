package com.anufriev.data.repository

import com.anufriev.data.entity.cloudMessageEntity.NotificationData
import com.anufriev.utils.platform.State
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body

interface FirebaseRepository {
    suspend fun postNotification(
        city: String, org: String, idOrg: Int = 0,
        onSuccess: (Boolean) -> Unit,
        onState: (State) -> Unit
    )
}