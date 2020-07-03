package com.mediary.activities.editquestions.presenter

import com.mediary.activities.editquestions.model.EditQuestionsModel
import com.mediary.activities.editquestions.view.EditQuestionsView
import com.mediary.database.AppDatabase
import com.mediary.database.entity.Questions

class EditQuestionsPresenterImpl(private var editQuestionsView: EditQuestionsView,private var database: AppDatabase) :
    EditQuestionsPresenter {

    private var editQuestionsModel: EditQuestionsModel = EditQuestionsModel(this,database)

    override fun addQuestionsList(questions: Questions) {
        editQuestionsModel.addQuestionsToDB(questions.joined_date)
    }

    override fun updateQuestionsList(dateString:String) {
        editQuestionsModel.updateQuestions(dateString)
    }

    override fun showQuestionsList() {
        editQuestionsView.showAddedQuestionsList()
    }

    override fun showUpdatedQuestions() {
        editQuestionsView.showUpdatedQuestions()
    }
    override fun showPromptAlert() {
       editQuestionsView.showPromptAlert()
    }

    override fun updatedEditedQuestion( question: String?,dateString: String,uid:Int) {
        editQuestionsModel.updateEditedQuestionsAnswers(question, dateString,uid)
    }

    override fun showEmptyGridAlert() {
        editQuestionsView.showEmptyPromptAlert()    }

}