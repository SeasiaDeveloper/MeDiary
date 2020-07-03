package com.mediary.activities.editquestions.presenter

import com.mediary.database.entity.Questions

interface EditQuestionsPresenter {
    fun addQuestionsList(questions: Questions)
    fun updateQuestionsList(dateString:String)
    fun showQuestionsList()
    fun showUpdatedQuestions()
    fun showPromptAlert()
    fun updatedEditedQuestion(question: String?,dateString:String,uid:Int)
    fun showEmptyGridAlert()

}