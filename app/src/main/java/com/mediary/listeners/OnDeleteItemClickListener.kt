package com.mediary.listeners

import com.mediary.database.entity.Questions

interface OnDeleteItemClickListener {
    fun onItemClick(position: Int, question: Questions)
}