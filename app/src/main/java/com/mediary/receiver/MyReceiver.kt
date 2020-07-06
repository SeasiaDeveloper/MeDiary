package com.mediary.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mediary.R

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

     val intent1 = Intent(context, DailyNotificationsIntentService::class.java)
     context!!.startService(intent1)

     /* var notificationManagerUtil = NotificationManagerUtil()
        notificationManagerUtil.showReminderNotification(
            context!!, "It's time to write daily diary",
            notificationManagerUtil.NOTIFICATION_ID_REMINDERS
        )*/


    }
    /* val intent1 = Intent(context, DailyNotificationsIntentService::class.java)
     context!!.startService(intent1)*/
}

