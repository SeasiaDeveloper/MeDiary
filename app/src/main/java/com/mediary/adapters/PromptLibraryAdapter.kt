package com.mediary.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.customviews.CustomTextView
import com.mediary.database.AppDatabase
import com.mediary.database.entity.Questions
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.prompt_library_items_list.view.*


class PromptLibraryAdapter(
    private var mDataList: List<Questions>,
    private var database: AppDatabase,
    private var context: Context
) : RecyclerView.Adapter<PromptLibraryAdapter.PromptViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.prompt_library_items_list, parent, false)
        return PromptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: PromptViewHolder, position: Int) {
        val questionsData = mDataList[position]
        val colorSelected = PreferenceHandler.readInteger(context, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
           // holder.btnCheckbox.setBackgroundColor(colorSelected)
          //  val hexColor = java.lang.String.format("#%06X", 0xFFFFFF and colorSelected)

            holder.btnCheckbox.thumbDrawable.setColorFilter(colorSelected, PorterDuff.Mode.MULTIPLY)
            holder.btnCheckbox.trackDrawable.setColorFilter(colorSelected, PorterDuff.Mode.MULTIPLY)
        }
        if (questionsData != null) {
            holder.txtQuestions.text = questionsData.question
            holder.txtHeader.text = questionsData.categoryName
            holder.txtHeader.typeface = Typeface.DEFAULT_BOLD
            holder.btnCheckbox.tag = position
            holder.btnCheckbox.isChecked = questionsData.isSelected
            setHeaderValue(holder, questionsData, position)
            holder.btnCheckbox.setOnClickListener { v ->
                val pos = holder.btnCheckbox.tag as Int
                val listQuestions = database.questionsDao()
                    .getPromptQuestionsCount(true, mDataList[position].joined_date)
                if (listQuestions != null) {
                    if (mDataList[pos].isSelected) {
                        holder.btnCheckbox.isChecked = false
                        mDataList[pos].isSelected = false
                        database.questionsDao()
                            .updatePromptQuestions(false, mDataList[position].uid)
                    } else {
                        holder.btnCheckbox.isChecked = true
                        mDataList[pos].isSelected = true
                        database.questionsDao().updatePromptQuestions(true, mDataList[position].uid)
                    }
                }
            }
        }
    }

    fun setList(mQuestionAnswerList: List<Questions>) {
        mDataList = mQuestionAnswerList as MutableList<Questions>
        notifyDataSetChanged()
    }

    private fun setHeaderValue(holder: PromptViewHolder, questionsData: Questions, position: Int) {
        if (position < mDataList.size) {
            val name: String = questionsData.categoryName
            if (!TextUtils.isEmpty(name)) {
                if (position == 0) {
                    holder.txtHeader.text = name
                    holder.txtHeader.visibility = View.VISIBLE
                } else if (position > 0) {
                    if (!TextUtils.isEmpty(mDataList[position - 1].categoryName) && !TextUtils.isEmpty(
                            mDataList[position].categoryName
                        )
                    ) {
                        if (mDataList[position - 1].categoryName == mDataList[position].categoryName) {
                            holder.txtHeader.visibility = View.GONE
                        } else {
                            holder.txtHeader.text = mDataList[position].categoryName
                            holder.txtHeader.visibility = View.VISIBLE
                        }
                    } else {
                        if (!TextUtils.isEmpty(mDataList[position].categoryName)) {
                            holder.txtHeader.text = mDataList[position].categoryName
                            holder.txtHeader.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    class PromptViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtQuestions: CustomTextView = itemView.tvPromptQuestion
        val btnCheckbox: Switch = itemView.btnPrompt
        val txtHeader: CustomTextView = itemView.tvHeader
    }


}