package com.mediary.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.mediary.utils.Utilities
import com.mediary.database.AppDatabase
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_dashboard.*

abstract class BaseActivity : AppCompatActivity() {
    var database: AppDatabase? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        database = AppDatabase.getDatabase(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        /*  if (Constants.isColorButtonClicked) {
              val colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
              window.navigationBarColor = ContextCompat.getColor(this, colorSelected)
          }*/
        setupUI()
        hideKeyboard(handleKeyboard())
    }

    private fun changeAppStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.getWindow()
            val statusBarColor =
                PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
            if (statusBarColor == Color.BLACK && window.navigationBarColor === Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if(statusBarColor !=0) {
                window.statusBarColor = statusBarColor
            }

        }
    }

    protected fun addAnalytics(name: String) {
        /*   firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
               param(FirebaseAnalytics.Param.ITEM_NAME, name)*/
        //    }
        val bundle = Bundle()
        bundle.putString("FirebaseAnalytics", name)
        firebaseAnalytics.logEvent("Firebase_Analytics", bundle)
    }

    protected abstract fun getLayout(): Int

    protected abstract fun setupUI()

    protected abstract fun handleKeyboard(): View

    override fun onResume() {
        super.onResume()
        changeAppStatusBar()
    }
    private fun hideKeyboard(view: View) {
        view.setOnClickListener {
            Utilities.hideKeyboard(this@BaseActivity)
        }
    }
}