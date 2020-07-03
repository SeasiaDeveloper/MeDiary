package com.mediary.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.customviews.CustomEditText
import com.mediary.customviews.CustomTextView
import com.mediary.database.entity.Questions
import com.mediary.listeners.OnDeleteQuestionClickListener
import com.mediary.listeners.OnUpdatedItemClickListener
import kotlinx.android.synthetic.main.edit_questions_list_items.view.*

class EditQuestionsAdapter(
    private var mDataList: MutableList<Questions>,
    private var listener: OnDeleteQuestionClickListener,
    private var questionListener:OnUpdatedItemClickListener
) :
    RecyclerView.Adapter<EditQuestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.edit_questions_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }
    fun setList(mQuestionAnswerList: List<Questions>) {
        mDataList= mQuestionAnswerList as MutableList<Questions>
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionsData = mDataList[position]
        holder.txtQuestions.setText(questionsData.question)
        holder.imgDelete.setOnClickListener {
            listener.onDeleteClick(position,questionsData)
        }//
        holder.txtQuestions.setOnClickListener {
            questionListener.onUpdatedItemClick(position,questionsData)
        }
    }

    fun updateParticularItem(
        position: Int,
        txtQuestions: Questions,
        editText: String
    ) {
        listener.onUpdateEditQuestion(position,txtQuestions,editText)
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtQuestions: CustomTextView = itemView.tvEditQuestion
        val imgDelete: AppCompatImageView = itemView.imgCancel
    }

}