package com.mediary.activities.questionswithanswersscreen.presenter

import java.util.*

interface QuestionsWithAnswersPresenter {
    fun getMoodWeatherOption(dateString: Date)
    fun showMoodWeather(mood: String, weather: String)
}