package com.mediary.activities.questionswithanswersscreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.customviews.CustomTextView
import com.mediary.database.AppDatabase
import com.mediary.database.entity.Questions
import com.squareup.picasso.Picasso

class QuestionWithAnswersAdapter(
    private var mQuestionAnswerList: List<Questions>,
    private var dateString: String,
    private var database: AppDatabase,
    private var adapterPosition: Int
) : RecyclerView.Adapter<QuestionWithAnswersAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textQuestion: TextView
        val textAnswer: TextView
        var firstImage: ImageView
        var secondImage: ImageView
        var thirdImage: ImageView
        var fourthtImage: ImageView
        var fifthtImage: ImageView
        var viewLine: View

        init {
            textQuestion = itemView.findViewById<View>(R.id.textQuestion) as CustomTextView
            textAnswer = itemView.findViewById<View>(R.id.textAnswer) as CustomTextView
            firstImage = itemView.findViewById<View>(R.id.imageFirst) as ImageView
            secondImage = itemView.findViewById<View>(R.id.imageSecond) as ImageView
            thirdImage = itemView.findViewById<View>(R.id.imageThird) as ImageView
            fourthtImage = itemView.findViewById<View>(R.id.imageFourth) as ImageView
            fifthtImage = itemView.findViewById<View>(R.id.imageFifth) as ImageView
            viewLine = itemView.findViewById(R.id.view) as View
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.answers_with_images_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mQuestionAnswerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        adapterPosition=position
        if (mQuestionAnswerList[position].answers == "") {
            holder.textAnswer.visibility = View.GONE
        } else {
            holder.textAnswer.visibility = View.VISIBLE
            holder.textAnswer.text = mQuestionAnswerList[position].answers
        }

        val imageList =
            database.imagesDao().setImages(mQuestionAnswerList[position].uid, dateString)
        val givenAnswers = mQuestionAnswerList[position].answers.replace("\n", "")
        if (givenAnswers.isNotEmpty() || imageList.isNotEmpty()) {
            holder.textQuestion.text = mQuestionAnswerList[position].question
            holder.viewLine.visibility = View.VISIBLE
        }

        if (database.imagesDao().setImages(
                mQuestionAnswerList[position].uid,
                dateString
            ).size == 1
        ) {
            holder.firstImage.visibility = View.VISIBLE
            holder.secondImage.visibility = View.GONE
            holder.thirdImage.visibility = View.GONE
            holder.fourthtImage.visibility = View.GONE
            holder.fifthtImage.visibility = View.GONE
            Picasso.get().load(imageList[0].imagesPath).into(holder.firstImage)
        } else if (database.imagesDao().setImages(
                mQuestionAnswerList[position].uid,
                dateString
            ).size == 2
        ) {
            holder.firstImage.visibility = View.VISIBLE
            holder.secondImage.visibility = View.VISIBLE
            holder.thirdImage.visibility = View.GONE
            holder.fourthtImage.visibility = View.GONE
            holder.fifthtImage.visibility = View.GONE
            Picasso.get().load(imageList[0].imagesPath).into(holder.firstImage)
            Picasso.get().load(imageList[1].imagesPath).into(holder.secondImage)
        } else if (database.imagesDao().setImages(
                mQuestionAnswerList[position].uid,
                dateString
            ).size == 3
        ) {
            holder.firstImage.visibility = View.VISIBLE
            holder.secondImage.visibility = View.VISIBLE
            holder.thirdImage.visibility = View.VISIBLE
            holder.fourthtImage.visibility = View.GONE
            holder.fifthtImage.visibility = View.GONE
            Picasso.get().load(imageList[0].imagesPath).into(holder.firstImage)
            Picasso.get().load(imageList[1].imagesPath).into(holder.secondImage)
            Picasso.get().load(imageList[2].imagesPath).into(holder.thirdImage)

        } else if (database.imagesDao().setImages(
                mQuestionAnswerList[position].uid,
                dateString
            ).size == 4
        ) {
            holder.firstImage.visibility = View.VISIBLE
            holder.secondImage.visibility = View.VISIBLE
            holder.thirdImage.visibility = View.VISIBLE
            holder.fourthtImage.visibility = View.VISIBLE
            holder.fifthtImage.visibility = View.GONE
            Picasso.get().load(imageList[0].imagesPath).into(holder.firstImage)
            Picasso.get().load(imageList[1].imagesPath).into(holder.secondImage)
            Picasso.get().load(imageList[2].imagesPath).into(holder.thirdImage)
            Picasso.get().load(imageList[3].imagesPath).into(holder.fourthtImage)
        } else if (database.imagesDao().setImages(
                mQuestionAnswerList[position].uid,
                dateString
            ).size == 5
        ) {
            holder.firstImage.visibility = View.VISIBLE
            holder.secondImage.visibility = View.VISIBLE
            holder.thirdImage.visibility = View.VISIBLE
            holder.fourthtImage.visibility = View.VISIBLE
            holder.fifthtImage.visibility = View.VISIBLE
            Picasso.get().load(imageList[0].imagesPath).into(holder.firstImage)
            Picasso.get().load(imageList[1].imagesPath).into(holder.secondImage)
            Picasso.get().load(imageList[2].imagesPath).into(holder.thirdImage)
            Picasso.get().load(imageList[3].imagesPath).into(holder.fourthtImage)
            Picasso.get().load(imageList[4].imagesPath).into(holder.fifthtImage)
        } else {
            holder.firstImage.visibility = View.GONE
            holder.secondImage.visibility = View.GONE
            holder.thirdImage.visibility = View.GONE
            holder.fourthtImage.visibility = View.GONE
            holder.fifthtImage.visibility = View.GONE
        }
    }
}