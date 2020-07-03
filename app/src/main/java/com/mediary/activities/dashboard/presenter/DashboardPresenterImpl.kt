package com.mediary.activities.dashboard.presenter


import com.mediary.activities.dashboard.model.DashboardModel
import com.mediary.activities.dashboard.view.DashboardView
import com.mediary.database.AppDatabase
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions

class DashboardPresenterImpl(private var dashboardView: DashboardView, private var database: AppDatabase, private var mQuestionAnswerList: List<Questions>) : DashboardPresenter {

    private var dashboardModel: DashboardModel = DashboardModel(this, database)

    override fun addQuestions(dateString: String, hiddenData: Boolean, selectedData: Boolean): List<Questions> {
        dashboardModel.addQuestions(dateString, true, true,false)
        return mQuestionAnswerList
    }

    override fun showAddedQuestions(allQuestions: List<Questions>) {
        dashboardView.showAddedAnswers(allQuestions)
    }

    override fun addPromptQuestions(questionModel: List<PromptQuestion>) {
        dashboardModel.addPromptQuestions(questionModel)
    }


    override fun getMoodWeatherOptions(currentdate: String) {
        dashboardModel.getMoodWeatherOptionsCurrentDate(currentdate)
    }

    override fun showMoodWeatherOptions(emoji: String, weather: String) {
        dashboardView.showMoodWeatherData(emoji,weather)
    }

}