package com.mediary.activities.questionswithanswersscreen.model

import com.mediary.activities.questionswithanswersscreen.presenter.QuestionsWithAnswersPresenter
import com.mediary.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class QuestionsWithAnswersModel(
    private var questionsWithAnswersPresenter: QuestionsWithAnswersPresenter,
    private var database: AppDatabase
) {

    fun getMoodWeatherOptionsCurrentDate(currentDate: Date) {
        val dest = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val result = dest.format(currentDate)
        var emoji: String?=null
        var weather: String?
        emoji = database.weatherMoodDao().getMoodTypeByDate(result, "MOOD")
        weather = database.weatherMoodDao().getMoodTypeByDate(result, "WEATHER")
        if (emoji == null) {
            emoji = "null"
        }

        if (weather == null) {
            weather = "null"
        }
        questionsWithAnswersPresenter.showMoodWeather(emoji, weather)
    }
}