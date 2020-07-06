package com.mediary.receiver

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity

class DailyNotificationsIntentService : IntentService("DailyNotificationsIntentService") {
    private lateinit var builder: NotificationCompat.Builder
    private val NOTIFICATION_ID = 3
    var CHANNEL_ONE_ID: String = "channel_id_meDiary"
    var CHANNEL_ONE_NAME: String = "channel_name_meDiary"


    override fun onHandleIntent(intent: Intent?) {
        val notifyIntent = Intent(this, DashboardActivity::class.java)
        notifyIntent.putExtra("isFromNotification", 1)
        val pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        showReminderNotification(this, "It's time to write daily diary", NOTIFICATION_ID,pendingIntent)
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             builder = NotificationCompat.Builder(this, "M_CH_ID")
             builder.setContentTitle(getString(R.string.me_diary))
             builder.setContentText(getString(R.string.its_time_to_write_daily_diary))
             builder.setSmallIcon(R.mipmap.app_icon)
         } else {
             builder = NotificationCompat.Builder(this)
             builder.setAutoCancel(true)
                 .setDefaults(Notification.DEFAULT_ALL)
                 .setSmallIcon(R.mipmap.app_icon)
                 .setContentTitle(getString(R.string.me_diary))
             builder.setContentText(getString(R.string.its_time_to_write_daily_diary))
             //.setColor(this.getColor(R.color.white)
             val bigText = NotificationCompat.BigTextStyle()
                 builder.setContentIntent(pendingIntent)

         }
         val notificationCompat = builder.build()
         val managerCompat = NotificationManagerCompat.from(this)
         managerCompat.notify(NOTIFICATION_ID, notificationCompat)*/

    }

    fun showReminderNotification(
        context: Context,
        msg: String,
        id: Int,
        pendingIntent: PendingIntent
    ) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ONE_ID)
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle(msg)
            .setAutoCancel(false)
            .setSound(alarmTone)
            builder.setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNotExist(notificationManager)
        }
        val notification = builder.build()
        notification.defaults = Notification.DEFAULT_LIGHTS
        builder.setSound(alarmTone)
        notificationManager.notify(id, notification)
    }

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

}

