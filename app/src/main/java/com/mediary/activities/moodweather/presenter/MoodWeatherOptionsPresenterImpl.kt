package com.mediary.activities.moodweather.presenter

import com.mediary.activities.moodweather.model.MoodWeatherModel
import com.mediary.activities.moodweather.view.MoodWeatherOptionsView
import com.mediary.database.AppDatabase

class MoodWeatherOptionsPresenterImpl(
    private var moodWeatherOptionsView: MoodWeatherOptionsView,
    private var database: AppDatabase
) : MoodWeatherPresenter {

    override fun getUpdateCallback() {
        moodWeatherOptionsView.getUpdateDatabaseCallback()
    }

    private var moodWeatherModel: MoodWeatherModel = MoodWeatherModel(this, database)

    override fun insertOrUpdateMoodWeatherOptions(emojiType: String, weatherType: String,currentDate:String) {
        moodWeatherModel.insertUpdateData(emojiType, weatherType,currentDate)
    }
}