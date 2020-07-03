package com.mediary.activities.calenderview.view

import com.mediary.activities.calenderview.pojo.CalendarIcon

interface CalenderActivityView {
    fun getDaysMoodOptions(isTrue:Boolean,emoji:String,weather:String)
    fun getIconsData(list: ArrayList<CalendarIcon>)
}