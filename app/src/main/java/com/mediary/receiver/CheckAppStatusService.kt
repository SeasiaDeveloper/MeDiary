package com.mediary.receiver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.mediary.utils.PreferenceHandler

class CheckAppStatusService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        Log.d("ClearFromRecentService", "Service Started")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ClearFromRecentService", "Service Destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.e("ClearFromRecentService", "END")
        PreferenceHandler.writeBoolean(this, PreferenceHandler.IS_APP_DESTROYED, true)
        stopSelf()
    } //Code here
}