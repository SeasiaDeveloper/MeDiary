package com.mediary.activities.dashboard.model

import com.mediary.activities.dashboard.presenter.DashboardPresenter
import com.mediary.database.AppDatabase
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions

class DashboardModel(
    private var dashboardPresenter: DashboardPresenter,
    private var database: AppDatabase
) {
    /**
     * get questions from database
     */

    fun addQuestions(dateString: String, hiddenData: Boolean, selectedData: Boolean,deletedData:Boolean) {
        if ((database.questionsDao().getAllQuestions(dateString, hiddenData, selectedData,deletedData).size <=1)) {
            if (database.questionsDao().getAllQuestionsData().isNotEmpty()) {
                if (database.questionsDao().getAllQuestionsAccordingToDate(dateString).isEmpty()) {
                    for (i in database.questionsDao().getAllQuestionsData().indices) {
                        val promptQuestions = database.questionsDao().getAllQuestionsData()[i]
                        val isHidden: Boolean = i >= 11
                        val isDeleted:Boolean = i >= 11
                        val questions = Questions(
                            promptQuestions.question,
                            "",
                            dateString,
                            promptQuestions.id,
                            isHidden,
                            true,
                            "",
                            promptQuestions.categoryId,
                            promptQuestions.categoryName,
                            "false",
                            isDeleted
                        )
                        database.questionsDao().insert(questions)
                    }
                }
            }
        }
       dashboardPresenter.showAddedQuestions(database.questionsDao().getAllQuestions(dateString, false, true,false))
    }

    /**
     * insert prompt questions in database
     */
    fun addPromptQuestions(questionModel: List<PromptQuestion>) {
        if (database.questionsDao().getAllPromptQuestionsData().isEmpty()) {
            database.questionsDao().insertPromptQuestions(questionModel)
        }
    }

    /**
     * get mood and weather option of current day
     */
    fun getMoodWeatherOptionsCurrentDate(currentDate: String) {
        var emoji: String?
        var weather: String?
        emoji = database.weatherMoodDao().getMoodTypeByDate(currentDate, "MOOD")
        weather = database.weatherMoodDao().getMoodTypeByDate(currentDate, "WEATHER")
        if (emoji==null) {
            emoji = "null"
        }

        if (weather==null) {
            weather = "null"
        }
        dashboardPresenter.showMoodWeatherOptions(emoji, weather)
    }
}