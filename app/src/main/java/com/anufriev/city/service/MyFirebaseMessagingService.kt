package com.anufriev.city.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.anufriev.city.R
import com.anufriev.data.storage.Pref
import com.anufriev.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kirich1409.androidnotificationdsl.notification
import org.jetbrains.anko.activityManager
import java.io.IOException
import java.net.URL
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.data["city"]?.contains(Pref(this).city.toString()) == true)
            sendNotification(remoteMessage.notification, remoteMessage.data)
    }

    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        data: Map<String, String>
    ) {
        val bundle = Bundle().apply {
            putString("idOrg", data["idOrg"])
        }
        val intent = Intent(this, MainActivity::class.java)
            .apply {
                putExtras(bundle)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = notification(this, CHANNEL_ID, smallIcon = R.drawable.ic_baseline_info_24) {
            contentTitle(notification?.title ?: "")
            contentText(notification?.body ?: "")
            autoCancel(true)
            contentIntent(pendingIntent)
            priority(NotificationCompat.PRIORITY_HIGH)
            sound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            largeIcon(BitmapFactory.decodeResource(resources, R.drawable.dislike))
            color(getColor(R.color.colorAccent))
            lights(Color.RED, 1000, 300)
            defaults(Notification.DEFAULT_VIBRATE)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                setShowBadge(true)
                canShowBadge()
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            }

            notificationManager.createNotificationChannel(channel)
        }
        val notificationID = Random.nextInt()
        notificationManager.notify(notificationID, notification as Notification)
    }

    companion object {
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_ID = "firebase_id"
        private const val CHANNEL_DESC = "Firebase notification"
    }
}