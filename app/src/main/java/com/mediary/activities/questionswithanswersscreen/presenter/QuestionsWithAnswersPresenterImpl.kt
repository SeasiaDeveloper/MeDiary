package com.mediary.activities.questionswithanswersscreen.presenter

import com.mediary.activities.questionswithanswersscreen.model.QuestionsWithAnswersModel
import com.mediary.activities.questionswithanswersscreen.view.QuestionWithAnswersView
import com.mediary.database.AppDatabase
import java.util.*

class QuestionsWithAnswersPresenterImpl(
    private var questionsView: QuestionWithAnswersView,
    database: AppDatabase
) : QuestionsWithAnswersPresenter {

    private var dashboardModel: QuestionsWithAnswersModel = QuestionsWithAnswersModel(this, database)

    override fun showMoodWeather(mood: String, weather: String) {
        questionsView.showMoodWeatherOptions(mood, weather)
    }

    override fun getMoodWeatherOption(dateString: Date) {
        dashboardModel.getMoodWeatherOptionsCurrentDate(dateString)
    }
}