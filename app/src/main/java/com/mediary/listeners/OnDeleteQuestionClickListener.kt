package com.mediary.listeners

import com.mediary.customviews.CustomEditText
import com.mediary.database.AppDatabase
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions
import java.time.OffsetDateTime

interface OnDeleteQuestionClickListener {
    fun onDeleteClick(position: Int, question: Questions)
    fun onUpdateEditQuestion(position: Int,question: Questions,txtQuestions: String)
}