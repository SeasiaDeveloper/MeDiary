package com.mediary.activities.answerstoquestions.presenter

import com.mediary.activities.answerstoquestions.model.QuestionAnswerModel
import com.mediary.activities.answerstoquestions.view.QuestionAnswerView

import com.mediary.database.AppDatabase

class QuestionAnswerPresenterImpl(private var questionAnswerView: QuestionAnswerView, private var database: AppDatabase
) : QuestionAnswerPresenter {
    private var questionAnswerModel: QuestionAnswerModel = QuestionAnswerModel(this, database)

    override fun updateAnswers(answers: String?, question: String?,date:String) {
        questionAnswerModel.updateQuestionsAnswers(answers, question,date)
    }
}