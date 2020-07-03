package com.mediary.receiver

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity

class DailyNotificationsIntentService:IntentService("DailyNotificationsIntentService") {
    private lateinit var builder: NotificationCompat.Builder
    private val NOTIFICATION_ID = 3


    override fun onHandleIntent(intent: Intent?) {
        val notifyIntent = Intent(this, DashboardActivity::class.java)
        notifyIntent.putExtra("isFromNotification",1)
        val pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)

      /*  val builder = NotificationCompat.Builder(this, "M_CH_ID")
        builder.setContentTitle(getString(R.string.me_diary))
        builder.setContentText(getString(R.string.its_time_to_write_daily_diary))
        builder.setSmallIcon(R.mipmap.app_icon)
        val notifyIntent = Intent(this, DashboardActivity::class.java)
        notifyIntent.putExtra("isFromNotification",1)
        val pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)
        val notificationCompat = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)*/
    }
}

