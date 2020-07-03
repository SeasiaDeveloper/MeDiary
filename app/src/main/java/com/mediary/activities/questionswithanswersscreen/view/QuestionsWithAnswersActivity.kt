package com.mediary.activities.questionswithanswersscreen.view

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.activities.questionswithanswersscreen.adapter.QuestionWithAnswersAdapter
import com.mediary.activities.questionswithanswersscreen.presenter.QuestionsWithAnswersPresenter
import com.mediary.activities.questionswithanswersscreen.presenter.QuestionsWithAnswersPresenterImpl
import com.mediary.base.BaseActivity
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.database.entity.Questions
import com.mediary.utils.Constants
import com.mediary.utils.ImagesUtil
import com.mediary.utils.PreferenceHandler
import com.mediary.utils.Utilities
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.ques_with_ans_layout.*
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


class QuestionsWithAnswersActivity : BaseActivity(), QuestionWithAnswersView, View.OnClickListener {
    private lateinit var answers: String
    private val position: Int = 0
    private var mood: String? = ""
    private var weather: String? = ""
    private var dateOfTheWeek: String? = null
    private var year: String? = null
    private var intentdate: String? = null
    private lateinit var questionPresenter: QuestionsWithAnswersPresenter
    private var mQuestionAnswerList: List<Questions> = mutableListOf()
    private var imagesList: List<MeDairyImagesEntity> = mutableListOf()
    private val mQuestionsWithDataList: ArrayList<Questions> = ArrayList()
    private lateinit var date: Date
    private lateinit var dateString: String

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.imageHome -> {
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.top_bottom)
            }
            R.id.imageSendPdf -> {
                if (database != null) {
                    mQuestionAnswerList =
                        database!!.questionsDao().getAllQuestions(
                            dateString,
                            hiddenData = false,
                            selectedData = true,
                            isDeleted = false
                        )
                    for (j in mQuestionAnswerList.indices) {
                        answers = mQuestionAnswerList[j].answers
                        if (answers.isNotEmpty() || imagesList.isNotEmpty()) {
                            ImagesUtil.getPdfFromImages(
                                this@QuestionsWithAnswersActivity,
                                mQuestionAnswerList,
                                database!!,
                                dateString
                            )
                            ImagesUtil.shareFile(this@QuestionsWithAnswersActivity)
                        } else {
                            Toast.makeText(
                                this@QuestionsWithAnswersActivity,
                                getString(R.string.no_data_exist),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.ques_with_ans_layout
    }

    private fun getQuestionsData() {
        val dateFormat = SimpleDateFormat(getString(R.string.dd_mmm_yyyy), Locale.getDefault())
        val strDate = dateFormat.format(date)
        dateString = Utilities.dbDateFormat(strDate)
        if (database != null) {
            mQuestionAnswerList = database!!.questionsDao().getAllQuestionsAccordingToDate(dateString)
            imagesList = database!!.imagesDao().getImagesFromDB(dateString) as ArrayList<MeDairyImagesEntity>
            for (i in mQuestionAnswerList.indices) {
                if (mQuestionAnswerList[i].answers != "" || mQuestionAnswerList[i].currentTime != "" || database!!.imagesDao()
                        .setImages(
                            mQuestionAnswerList[i].uid,
                            dateString
                        ).isNotEmpty()
                ) {
                    mQuestionsWithDataList.add(mQuestionAnswerList[i])
                }
            }
            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            layoutManager.stackFromEnd = false
            questionDataRecyclerView.layoutManager = layoutManager
            val adapter = QuestionWithAnswersAdapter(mQuestionsWithDataList, dateString, database!!,position)
            questionDataRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.HORIZONTAL
                )
            )
            questionDataRecyclerView.adapter = adapter
        }
        val colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            toolbar.setBackgroundColor(colorSelected)
        }
    }

    private fun setDayDate(date: Date) {
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        dateOfTheWeek = dateFormat.format(date)
        textDateDashboard.text = dateOfTheWeek
        textDateDashboard.typeface = Typeface.DEFAULT_BOLD
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayOfTheWeek = dayFormat.format(date)
        textDay.text = dayOfTheWeek
        val monthFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
        year = monthFormat.format(date)
        textYear.text = year
        questionPresenter = QuestionsWithAnswersPresenterImpl(this, database!!)
        questionPresenter.getMoodWeatherOption(date)
        getQuestionsData()
    }

    override fun setupUI() {
        intentdate = intent.extras?.getString(Constants.DISPLAYED_DATE)
        val dest = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
        try {
            date = dest.parse(intentdate)
        } catch (e: ParseException) {
            Log.d("Exception", e.message!!)
        }
        setDayDate(date)
        imageHome.setOnClickListener(this)
        imageSendPdf.setOnClickListener(this)
    }

    override fun handleKeyboard(): View {
        return questionAnswersLayout
    }

    override fun showMoodWeatherOptions(mood: String, weather: String) {
        this.mood = mood
        this.weather = weather
        if (this.mood.equals("null", false)) {
            this.mood = getString(R.string.happy)
        }

        if (this.weather.equals("null", false)) {
            this.weather = getString(R.string.sunny)
        }

        when {
            this.mood.equals(getString(R.string.happy)) -> {
                imageOfMood?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.happy))
                imageOfMood?.setColorFilter(
                    ContextCompat.getColor(this@QuestionsWithAnswersActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.mood.equals(getString(R.string.cool)) -> {
                imageOfMood?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cool))
                imageOfMood?.setColorFilter(
                    ContextCompat.getColor(this@QuestionsWithAnswersActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.mood.equals(getString(R.string.neutral)) -> {
                imageOfMood?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.neutral))
                imageOfMood?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.mood.equals(getString(R.string.sad)) -> {
                imageOfMood?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sad))
                imageOfMood?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.mood.equals(getString(R.string.boring)) -> {
                imageOfMood?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.boring))
                imageOfMood?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.mood.equals(getString(R.string.angry)) -> {
                imageOfMood?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.angry))
                imageOfMood?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }

        when {
            this.weather.equals(getString(R.string.cloudy)) -> {
                imageOfWeather?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cloudy))
                imageOfWeather?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.weather.equals(getString(R.string.rainy)) -> {
                imageOfWeather?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rain))
                imageOfWeather?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.weather.equals(getString(R.string.snowy)) -> {
                imageOfWeather?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.snowy))
                imageOfWeather?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.weather.equals(getString(R.string.sunny)) -> {
                imageOfWeather?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sunny))
                imageOfWeather?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.weather.equals(getString(R.string.thunder)) -> {
                imageOfWeather?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.thunder
                    )
                )
                imageOfWeather?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            this.weather.equals(getString(R.string.windy)) -> {
                imageOfWeather?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.windy))
                imageOfWeather?.setColorFilter(
                    ContextCompat.getColor(
                        this@QuestionsWithAnswersActivity,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            toolbar.setBackgroundColor(colorSelected)
        }else{
            toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }
    }
}