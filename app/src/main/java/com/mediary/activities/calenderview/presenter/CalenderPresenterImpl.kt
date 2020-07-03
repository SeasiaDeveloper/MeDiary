package com.mediary.activities.calenderview.presenter

import android.content.Context
import com.mediary.activities.calenderview.model.CalenderModel
import com.mediary.activities.calenderview.pojo.CalendarIcon
import com.mediary.activities.calenderview.view.CalenderActivityView
import com.mediary.database.AppDatabase
import kotlin.collections.ArrayList

class CalenderPresenterImpl(private var calenderActivityView: CalenderActivityView, private var database: AppDatabase) :
    CalenderPresenter {
    override fun getIconsToSetOnCalender(list: ArrayList<CalendarIcon>) {
        calenderActivityView.getIconsData(list)
    }

    override fun setIconsOnCalender(mContext: Context, list:ArrayList<CalendarIcon>) {
       calenderModel.getIconsToSetOnCalender(mContext,list)
    }

    override fun getMoodOptions(isTrue: Boolean, emoji: String, weather: String) {
        calenderActivityView.getDaysMoodOptions(isTrue, emoji, weather)
    }

    private var calenderModel = CalenderModel(this, database)

    override fun setMoodOptionWeatherOption() {
        calenderModel.getMoodWeatherOptions()
    }




}