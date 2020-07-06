package com.mediary.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mediary.R

class NotificationManagerUtil {

    var NOTIFICATION_ID_REMINDERS: Int = 205
    var CHANNEL_ONE_ID: String = "channel_id_meDiary"
    var CHANNEL_ONE_NAME: String = "channel_name_meDiary"


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannelIfNotExist(notificationManager: NotificationManager) {
        val alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(
            CHANNEL_ONE_ID,
            CHANNEL_ONE_NAME, importance
        )
        notificationChannel.enableLights(true)
        notificationChannel.setShowBadge(true)
        notificationChannel.enableVibration(false)

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        notificationChannel.setSound(soundUri, audioAttributes)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)

    }

    fun cancelNotification(context: Context, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }

    fun showReminderNotification(
        context: Context,
        msg: String,
        id: Int
    ) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ONE_ID)
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle(msg)
            .setAutoCancel(false)
            .setSound(alarmTone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNotExist(notificationManager)
        }
        val notification = builder.build()
        notification.defaults = Notification.DEFAULT_LIGHTS
        builder.setSound(alarmTone)
        notificationManager.notify(id, notification)
    }
}