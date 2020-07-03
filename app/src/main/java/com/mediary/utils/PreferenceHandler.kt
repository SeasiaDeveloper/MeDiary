package com.mediary.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHandler {
    private const val PREF_NAME = "me_diary"
    private const val MODE = Context.MODE_PRIVATE
    const val IS_INSTALLED = "isInstalled"
    const val IS_FIRST_TIME = "is_first_time"
    const val COLOR_SELECTION="selected_color"
    const val IS_QUES_FIRST_TIME = "is_ques_first_time"
    const val IS_APP_DESTROYED = "is_app_destroyed"
    const val REMINDER_STATUS = "reminder_status"


    fun writeBoolean(context: Context, key: String, value: Boolean) {
        getEditor(context).putBoolean(key, value).commit()
    }

    fun readBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        return getPreferences(context).getBoolean(key, defValue)
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, MODE)
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    fun writeInteger(context: Context, key: String, value: Int) {
        getEditor(context).putInt(key, value).commit()
    }

    fun readInteger(context: Context, key: String, defValue: Int): Int {
        return getPreferences(context).getInt(key, defValue)
    }

}