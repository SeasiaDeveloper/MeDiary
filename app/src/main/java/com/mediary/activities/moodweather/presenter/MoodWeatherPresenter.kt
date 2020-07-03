package com.mediary.activities.moodweather.presenter

interface MoodWeatherPresenter {
    fun insertOrUpdateMoodWeatherOptions(emojiType: String, weatherType: String,date:String)
    fun getUpdateCallback()
}