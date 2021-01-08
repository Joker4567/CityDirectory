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
import org.jetbrains.anko.activityManager
import java.io.IOException
import java.net.URL
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var numMessages = 0
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        if(notification!!.title!!.contains(Pref(this).city.toString()))
            sendNotification(notification, data)
    }

    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        data: Map<String, String>
    ) {
        val bundle = Bundle()
        bundle.putString(
            "idOrg",
            data["idOrg"]
        )
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder =
            NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(notification!!.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dislike))
                .setColor(getColor(R.color.colorAccent))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_baseline_info_24)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }
        assert(notificationManager != null)
        val notificationID = Random.nextInt()
        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    companion object {
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
    }
}