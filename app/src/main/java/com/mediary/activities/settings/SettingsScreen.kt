package com.mediary.activities.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.mediary.R
import com.mediary.activities.ColorChooseActivity
import com.mediary.activities.dashboard.DashboardActivity
import com.mediary.base.BaseActivity
import com.mediary.listeners.OnCustomizeColorClickListener
import com.mediary.listeners.OnItemSelected
import com.mediary.receiver.MyReceiver
import com.mediary.utils.Alert
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsScreen : BaseActivity(), View.OnClickListener, OnCustomizeColorClickListener,
    OnItemSelected {

    private val NOTIFICATION_REMINDER_NIGHT = 2
    private lateinit var alerts: Alert
    var isAppTourClicked = false

    override fun getLayout(): Int {
        return R.layout.activity_settings
    }

    override fun setupUI() {
        alerts = Alert(this)
        toolbar.title = getString(R.string.settings)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        var reminderStatus = PreferenceHandler.readBoolean(this,PreferenceHandler.REMINDER_STATUS,true)
        if (reminderStatus==true) {
            switchReminders.isChecked=true
        } else {
            switchReminders.isChecked=false

        }

        switchReminders.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setDailyAlarm()
                PreferenceHandler.writeBoolean(this,PreferenceHandler.REMINDER_STATUS,true)
            } else {
            cancelAlarm()
                PreferenceHandler.writeBoolean(this,PreferenceHandler.REMINDER_STATUS,false)
            }
        })
        tvAppTour.setOnClickListener(this)
        changeThemeColor.setOnClickListener(this)
        shareApp.setOnClickListener(this)
        rateApp.setOnClickListener(this)
        tvOtherApps.setOnClickListener(this)
     }

    override fun handleKeyboard(): View {
        return linear_settings
    }

    private fun setDailyAlarm() {
        val notifyIntent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_REMINDER_NIGHT,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmStartTime = Calendar.getInstance()
        val now = Calendar.getInstance()
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 6)
        alarmStartTime.set(Calendar.MINUTE, 0)
        alarmStartTime.set(Calendar.SECOND, 0)
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            1000 * 60,
            pendingIntent
        )
    }


    fun cancelAlarm() {
        var intent = Intent(this, MyReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_REMINDER_NIGHT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent);
    }



    override fun onResume() {
        super.onResume()
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            toolbar.setBackgroundColor(colorSelected)
        }
        if (colorSelected != 0) {
            switchReminders.thumbDrawable.setColorFilter(colorSelected, PorterDuff.Mode.MULTIPLY)
            switchReminders.trackDrawable.setColorFilter(colorSelected, PorterDuff.Mode.MULTIPLY)
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.shareApp -> {
                val appId = this.packageName
                val myIntent = Intent(Intent.ACTION_SEND)
                myIntent.type = "text/plain"
                val body = "https://play.google.com/store/apps/details?id=$appId"
                val sub = getString(R.string.me_diary)
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub)
                myIntent.putExtra(Intent.EXTRA_TEXT, body)
                startActivity(Intent.createChooser(myIntent, getString(R.string.share_using)))
            }

            R.id.rateApp -> {
                launchMarket()
            }

            R.id.changeThemeColor -> {
            alerts.showCustomizeColorAlert(this)
        }

            R.id.tvAppTour -> {
                isAppTourClicked = true
                PreferenceHandler.writeInteger(this, PreferenceHandler.IS_FIRST_TIME, 0)
                var mIntent = Intent(this, DashboardActivity::class.java)
               // mIntent.putExtra(Constants.INTENT_FROM,"from_settings")
                startActivity(mIntent)
            }

            R.id.tvOtherApps -> {
                alerts.showCustomizeOtherAppsAlert(this)
            }

        }
    }

 /*   override fun onBackPressed() {
        if (!isAppTourClicked){
            super.onBackPressed()
        } else {
            finish()
        }

    }*/

    private fun launchMarket() {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val myAppLinkToMarket =
            Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
        }
    }

    override fun onColorChangeClick() {
        startActivity(Intent(this, ColorChooseActivity::class.java))

    }

    override fun onItemClick(appname: String) {

    }
 }