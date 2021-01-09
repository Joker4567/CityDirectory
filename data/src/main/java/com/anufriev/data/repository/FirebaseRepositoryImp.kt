package com.anufriev.data.repository

import com.anufriev.data.entity.cloudMessageEntity.NotificationData
import com.anufriev.data.network.NotificationAPI
import com.anufriev.utils.platform.BaseRepository
import com.anufriev.utils.platform.ErrorHandler
import com.anufriev.utils.platform.State
import com.anufriev.utils.services.CloudMessage
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class FirebaseRepositoryImp(
    errorHandler: ErrorHandler,
    private val api: NotificationAPI
) : BaseRepository(errorHandler = errorHandler), FirebaseRepository {

    override suspend fun postNotification(
        city: String, org: String, idOrg: Int,
        onSuccess: (Boolean) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
//            val url = "https://gcm-http.googleapis.com/fcm/send"
//            val jsonObjectString = Gson().toJson(notification.toString())
//            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
//            api.postNotification(url,requestBody)

            val jPayload = JSONObject()
            val jNotification = JSONObject()
            val jData = JSONObject()
            jNotification.put("title", "$city")
            jNotification.put("body", "Рейтинг организации $org был понижен")
            jNotification.put("sound", "default")
            jNotification.put("badge", "1")
            jNotification.put("click_action", "OPEN_ACTIVITY_1")
            jNotification.put("icon", "ic_notification")
            jData.put("idOrg", idOrg)
            jPayload.put("to", "/topics/news")
            jPayload.put("priority", "high")
            jPayload.put("notification", jNotification)
            jPayload.put("data", jData)
            val url = URL("https://fcm.googleapis.com/fcm/send")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", CloudMessage.AUTH_KEY)
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            // Send FCM message content.
            val outputStream = conn.outputStream
            outputStream.write(jPayload.toString().toByteArray())

            // Read FCM response.
            val inputStream = conn.inputStream
            val resp = convertStreamToString(inputStream)
            true
        }
    }

    private fun convertStreamToString(`is`: InputStream): String {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next().replace(",", ",\n") else ""
    }
}