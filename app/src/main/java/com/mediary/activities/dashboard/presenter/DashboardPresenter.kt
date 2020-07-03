package com.mediary.activities.dashboard.presenter

import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions

interface DashboardPresenter {
    fun addQuestions(dateString: String,hiddenData: Boolean, selectedData: Boolean): List<Questions>
    fun showAddedQuestions(allQuestions: List<Questions>)
    fun addPromptQuestions(questionModel: List<PromptQuestion>)
    fun getMoodWeatherOptions(currentDate:String)
    fun showMoodWeatherOptions(emoji:String,weather:String)
}