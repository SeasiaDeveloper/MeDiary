package com.mediary.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mediary.R
import com.mediary.customviews.CustomTextView
import com.mediary.database.AppDatabase
import com.mediary.database.entity.Questions
import com.mediary.listeners.OnGridItemClickListener
import kotlinx.android.synthetic.main.dashboard_grid_items_layout.view.*


class DashboardGridAdapter(
    private var mDataList: List<Questions>,
    private var listener: OnGridItemClickListener,
    private var database: AppDatabase,
    var context:Context
) :
    RecyclerView.Adapter<DashboardGridAdapter.GridViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_grid_items_layout, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val questionsData = mDataList[position]
        holder.txtQuestions.text = questionsData.question
        holder.txtAnswers.text = questionsData.answers
        holder.txtTime.text = questionsData.currentTime
        questionsData.joined_date
        val imagesList = database.imagesDao().setImages(questionsData.uid, questionsData.joined_date)
        if (imagesList.isNullOrEmpty()) {
            holder.imagesLayout.visibility = View.GONE
        } else {
            holder.imagesLayout.visibility = View.VISIBLE
            if (imagesList[0].imagesPath.isNotEmpty()) {
                holder.imgView1.visibility = View.VISIBLE
                holder.txtQuestions.setTextColor(Color.BLACK)
                Glide.with(holder.imgView1.context)
                    .load(imagesList[0].imagesPath)
                    .into(holder.imgView1)
            } else {
                holder.imgView1.visibility = View.GONE
            }
            if (imagesList.size > 1) {
                holder.txtQuestions.setTextColor(Color.BLACK)
                if (imagesList[1].imagesPath.isNotEmpty()) {
                    holder.imgView2.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(imagesList[1].imagesPath)
                        .into(holder.imgView2)
                } else {
                    holder.imgView2.visibility = View.GONE
                }
            }
            if (imagesList.size > 2) {
                holder.txtQuestions.setTextColor(Color.BLACK)
                if (imagesList[2].imagesPath.isNotEmpty()) {
                    holder.imgView3.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(imagesList[2].imagesPath)
                        .into(holder.imgView3)
                } else {
                    holder.imgView3.visibility = View.GONE
                }
            }
            if (imagesList.size > 3) {
                holder.txtQuestions.setTextColor(Color.BLACK)
                if (imagesList[3].imagesPath.isNotEmpty()) {
                    holder.imgView4.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(imagesList[3].imagesPath)
                        .into(holder.imgView4)
                } else {
                    holder.imgView4.visibility = View.GONE
                }
            }
            if (imagesList.size > 4) {
                holder.txtQuestions.setTextColor(Color.BLACK)
                if (imagesList[4].imagesPath.isNotEmpty()) {
                    holder.imgView5.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(imagesList[4].imagesPath)
                        .into(holder.imgView5)
                } else {
                    holder.imgView5.visibility = View.GONE
                }
            }
        }

        holder.itemView.setOnClickListener { view ->

            listener.onItemClick(mDataList, position, view)
        }
        holder.txtQuestions.setTextColor(ContextCompat.getColor(context,R.color.grey))

        val answersData = questionsData.answers
        val newAnswers = answersData.replace("\n", "")
        if (newAnswers.isNotEmpty()&& newAnswers.isNotBlank()) {
                holder.txtQuestions.setTextColor(Color.BLACK)
                holder.txtAnswers.setTextColor(Color.BLACK)

        }
        if (imagesList.size==1 || imagesList.size > 1) {
            holder.txtQuestions.setTextColor(Color.BLACK)
        }
    }

    fun setList(mQuestionAnswerList: List<Questions>) {
        mDataList = mQuestionAnswerList
        notifyDataSetChanged()
    }

    class GridViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtQuestions: CustomTextView = itemView.tvQuestion
        val txtAnswers: CustomTextView = itemView.tvAnswers
        val txtTime: CustomTextView = itemView.tvTime
        val imgView1: ImageView = itemView.img1
        val imgView2: ImageView = itemView.img2
        val imgView3: ImageView = itemView.img3
        val imgView4: ImageView = itemView.img4
        val imgView5: ImageView = itemView.img5
        val imagesLayout: LinearLayout = itemView.imgLayout
    }
}