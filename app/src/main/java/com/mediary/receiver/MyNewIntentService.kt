package com.mediary.receiver

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity

class MyNewIntentService:IntentService("MyNewIntentService") {
    private val NOTIFICATION_ID = 3


    override fun onHandleIntent(intent: Intent?) {
        val builder = Notification.Builder(this)
        builder.setContentTitle("MeDiary")
        builder.setContentText("It's time to write daily diary")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val notifyIntent = Intent(this, DashboardActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)
    }
}

