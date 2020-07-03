package com.mediary.listeners

import com.mediary.database.entity.Questions

interface OnUpdateQuestionClickListener {
    fun onUpdatedQuestionClick(position: Int,questionText:String, question: Questions)
}