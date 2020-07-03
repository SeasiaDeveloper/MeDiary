package com.mediary.listeners

import android.view.View
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions

interface OnGridItemClickListener {
    fun onItemClick(questionData:List<Questions>,position: Int,view: View)
}