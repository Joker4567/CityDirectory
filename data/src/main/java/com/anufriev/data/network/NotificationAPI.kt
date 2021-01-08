package com.anufriev.data.network

import com.anufriev.data.entity.cloudMessageEntity.NotificationData
import com.anufriev.utils.services.CloudMessage.Companion.CONTENT_TYPE
import com.anufriev.utils.services.CloudMessage.Companion.SERVER_KEY
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

//Firebase API
interface NotificationAPI {
    @Headers(
        "Authorization: key=$SERVER_KEY",
        "Content-Type:$CONTENT_TYPE"
    )
    @POST()
    suspend fun postNotification(
        @Url url: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}