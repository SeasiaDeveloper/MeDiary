package com.mediary.activities.calenderview.presenter

import android.content.Context
import com.mediary.activities.calenderview.pojo.CalendarIcon

interface CalenderPresenter {
    fun setMoodOptionWeatherOption()
    fun getMoodOptions(isTrue:Boolean,emoji:String,weather:String)
    fun setIconsOnCalender(mContext: Context,list:ArrayList<CalendarIcon>)
    fun getIconsToSetOnCalender(list:ArrayList<CalendarIcon>)
}