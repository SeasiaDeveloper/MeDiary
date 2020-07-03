package com.mediary.activities.editquestions.model

import com.mediary.activities.editquestions.presenter.EditQuestionsPresenter
import com.mediary.database.AppDatabase

class EditQuestionsModel(
    private var editQuestionPresenter: EditQuestionsPresenter,
    private var database: AppDatabase
) {

    /**
     * add questions in database
     */
    fun addQuestionsToDB(dateString: String) {
        val question = database.questionsDao().getQuestionFromPrompt(dateString, false, true, false)
        editQuestionPresenter.addQuestionsList(question)
    }
    /**
     * update questions in database
     */
    fun updateQuestions(dateString: String) {
        val question = database.questionsDao().getQuestion(dateString, true, true)
        if(question !=null) {
            database.questionsDao().updateQuestions(false, true, false, question.uid)
            editQuestionPresenter.showUpdatedQuestions()
        }else{
            editQuestionPresenter.showEmptyGridAlert()
        }
    }

    /**
     * update edited qusetions in database
     *
     */
    fun updateEditedQuestionsAnswers(question: String?, date: String, uid: Int) {
        database.questionsDao().updateEditedQuestion(question,date,uid)
    }
       /* val questionsData = database.questionsDao().getQuestion(date, true, true)
            database.questionsDao().updateEditedQuestion(question,date,questionsData.uid)
        }*/

}