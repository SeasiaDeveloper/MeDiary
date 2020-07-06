package com.mediary.listeners

import android.view.View
import com.mediary.database.entity.Questions

interface OnGridItemClickListener {
    fun onItemClick(questionData:List<Questions>,position: Int,view: View)

    fun onItemsinflate(position: Int, itemView: View)
}