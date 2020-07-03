package com.mediary.activities.answerstoquestions.model

import com.mediary.activities.answerstoquestions.presenter.QuestionAnswerPresenter
import com.mediary.database.AppDatabase

class QuestionAnswerModel(private var questionAnswerPresenter: QuestionAnswerPresenter, private var database: AppDatabase) {

    /**
     * update answers of questions in database
     */
    fun updateQuestionsAnswers(answers: String?, question: String?,date:String) {
        if (database.questionsDao().getAllQuestionsData().isNotEmpty()) {
            database.questionsDao().update(answers, question,date)
        }
    }
}