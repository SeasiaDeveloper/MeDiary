package com.mediary.activities.dashboard.view

import com.mediary.database.entity.Questions

interface DashboardView {
    fun showAddedAnswers(allQuestions: List<Questions>)
    fun showMoodWeatherData(emoji:String,weather:String)
}