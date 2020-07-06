package com.mediary.receiver

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity

class CheckRecentRun:Service() {
    private val TAG = "CheckRecentPlay"
    private val NOTIFICATION_REMINDER = 3
    private val MILLISECS_PER_DAY = 86400000L
      private val delay = MILLISECS_PER_DAY * 7 // 7 days
    private val NOTIFICATION_ID = 4
    var CHANNEL_ONE_ID: String = "channel_id_meDiary"
    var CHANNEL_ONE_NAME: String = "channel_name_meDiary"

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "Service started")
        val settings = getSharedPreferences(DashboardActivity.PREFS, MODE_PRIVATE)
        // Are notifications enabled?
        if (settings.getBoolean("enabled", true))
        {
            // Is it time for a notification?
            if (settings.getLong("lastRun", java.lang.Long.MAX_VALUE) < System.currentTimeMillis() - delay)
              //  sendNotification()
                showReminderNotification()
        }
        else
        {
            Log.i(TAG, "Notifications are disabled")
        }
        // Set an alarm for the next time this service should run:
        setAlarm()
        Log.v(TAG, "Service stopped")
        stopSelf()
    }

    fun setAlarm() {
        val serviceIntent = Intent(this, CheckRecentRun::class.java)
        val pi = PendingIntent.getService(this, NOTIFICATION_REMINDER, serviceIntent,
            PendingIntent.FLAG_CANCEL_CURRENT)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + delay, pi)
        Log.v(TAG, "Alarm set")
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

    fun showReminderNotification() {
        val mainIntent = Intent(this, DashboardActivity::class.java)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, CHANNEL_ONE_ID)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle(getString(R.string.notification_me_diary))
            .setContentText(getString(R.string.we_are_missing_you_hope_everything_fine))
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.mipmap.app_icon)
            .setTicker(getString(R.string.please_come_back_fill_your_daily_diary))
            .setWhen(System.currentTimeMillis())
            builder.setSound(alarmTone)
            .notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNotExist(notificationManager)
        }
        val notification = builder.build()
        notification.defaults = Notification.DEFAULT_LIGHTS

        notificationManager.notify(NOTIFICATION_ID, notification)
    }



    private fun sendNotification() {
        val mainIntent = Intent(this, DashboardActivity::class.java)
        val noti = NotificationCompat.Builder(this,"M_CH_ID")
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle(getString(R.string.notification_me_diary))
            .setContentText(getString(R.string.we_are_missing_you_hope_everything_fine))
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.mipmap.app_icon)
            .setTicker(getString(R.string.please_come_back_fill_your_daily_diary))
            .setWhen(System.currentTimeMillis())
            .notification
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(131315, noti)
        Log.v(TAG, "Notification sent")
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}