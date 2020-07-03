package com.mediary

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mediary.utils.MyColor

class MeDiaryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log("message")
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        // firebaseAnalytics = Firebase.analytics
        savedColor = MyColor()
    }

    companion object {
        @JvmField
        var isLightsOn: Boolean = false
        lateinit var savedColor: MyColor
    }
 }