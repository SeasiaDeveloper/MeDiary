package com.mediary.listeners

import com.mediary.database.entity.Questions

interface OnUpdatedItemClickListener {
   fun onUpdatedItemClick(position: Int, question: Questions)
}