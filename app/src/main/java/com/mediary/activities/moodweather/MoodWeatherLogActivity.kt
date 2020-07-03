package com.mediary.activities.moodweather

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.mediary.R
import com.mediary.activities.moodweather.presenter.MoodWeatherOptionsPresenterImpl
import com.mediary.activities.moodweather.view.MoodWeatherOptionsView
import com.mediary.activities.moodweather.view.MoodWeatherView
import com.mediary.adapters.MoodAdapter
import com.mediary.adapters.MoodWeatherAdapter
import com.mediary.base.BaseActivity
import com.mediary.models.MoodOptions
import com.mediary.models.WeatherOptions
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_mood_weather_log.*
import java.util.*

class  MoodWeatherLogActivity : BaseActivity(), MoodWeatherView, MoodWeatherOptionsView {
    private var colorSelected: Int=0
    private var emojiType: String = ""
    private var weatherType: String = ""
    private var currentDate: String = ""
    private var isExist: Boolean = false
    private var isWeatherExist: Boolean = false
    private lateinit var moodWeatherPresenter: MoodWeatherOptionsPresenterImpl
    private var myEmojiList: ArrayList<MoodOptions> = ArrayList()
    private var myWeatherList: ArrayList<WeatherOptions> = ArrayList()

    override fun showWeatherOptions(position: Int) {
        if (position == -1) {
            weatherType == null
            choosed_weather.text = getString(R.string.weather_s)
            choosed_weather.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            weatherType = myWeatherList[position].type
            choosed_weather.text = myWeatherList[position].type
            if (colorSelected != 0){
                choosed_weather.setTextColor(colorSelected)
            }else {
                choosed_weather.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }        }
    }

    override fun showMoodOptions(position: Int) {
        if (position == -1) {
            emojiType == null
            choosed_emoji.text = getString(R.string.mood_s)
            choosed_emoji.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            emojiType = myEmojiList[position].type
            choosed_emoji.text = myEmojiList[position].type
            if (colorSelected != 0){
                choosed_emoji.setTextColor(colorSelected)
            }else {
                choosed_emoji.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.activity_mood_weather_log
    }

    override fun setupUI() {
        if (database != null) {
            moodWeatherPresenter = MoodWeatherOptionsPresenterImpl(this, database!!)
        }
         colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0){
            btn_done.setBackgroundColor(colorSelected)
    }else
    {
        btn_done.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    val numberOfColumns = 3
    var selectedMoodPosition = 0
    var selectedWeatherPosition = -1
    val intent: Intent = intent
    emojiType = intent.getStringExtra(getString(R.string.mood))!!
    weatherType = intent.getStringExtra(getString(R.string.weather))!!
    currentDate = intent.getStringExtra(getString(R.string.current_date))!!
    tvDate.text = currentDate
         colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0){
            tvDate.setTextColor(colorSelected)
                             }
    if (emojiType == "null")
    {
        choosed_emoji.text = getString(R.string.mood_s)
        choosed_emoji.setTextColor(ContextCompat.getColor(this, R.color.black))
    } else
    {
        choosed_emoji.text = emojiType
        if (colorSelected != 0){
            choosed_emoji.setTextColor(colorSelected)
        }else {
            choosed_emoji.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
    }
    if (weatherType == "null") {
        choosed_weather.text = getString(R.string.weather_s)
        choosed_weather.setTextColor(ContextCompat.getColor(this, R.color.black))
    } else
    {
        if (colorSelected != 0){
            choosed_weather.text = weatherType
            choosed_weather.setTextColor(colorSelected)
        }else {
            choosed_weather.text = weatherType
            choosed_weather.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
    }
    when (emojiType)
    {
        getString(R.string.angry) -> selectedMoodPosition = 0
        getString(R.string.cool) -> selectedMoodPosition = 1
        getString(R.string.happy) -> selectedMoodPosition = 2
        getString(R.string.neutral) -> selectedMoodPosition = 3
        getString(R.string.sad) -> selectedMoodPosition = 4
        getString(R.string.boring) -> selectedMoodPosition = 5
    }

    when (weatherType)
    {
        getString(R.string.cloudy) -> selectedWeatherPosition = 0
        getString(R.string.rainy) -> selectedWeatherPosition = 1
        getString(R.string.snowy) -> selectedWeatherPosition = 2
        getString(R.string.sunny) -> selectedWeatherPosition = 3
        getString(R.string.thunder) -> selectedWeatherPosition = 4
        getString(R.string.windy) -> selectedWeatherPosition = 5
    }

    if (emojiType != "null")
    {
        isExist = true
    }
    if (weatherType != "null")
    {
        isWeatherExist = true
    }

    gv_weather.layoutManager = GridLayoutManager(this, numberOfColumns)
    val weatherAdapter =
        MoodWeatherAdapter(
            this@MoodWeatherLogActivity,
            myWeatherList,
            this,
            selectedWeatherPosition,
            isWeatherExist
        )
    gv_weather.adapter = weatherAdapter
    addWeatherIcons()

    gv_mood.layoutManager = GridLayoutManager(this, numberOfColumns)
    val moodAdapter =
        MoodAdapter(
            this@MoodWeatherLogActivity,
            myEmojiList,
            this,
            selectedMoodPosition,
            isExist
        )
    gv_mood.adapter = moodAdapter
    addMoodIcons()

    btn_done.setOnClickListener()
    {
        if (emojiType == "") {
            emojiType = getString(R.string.happy)
        }
        if (weatherType == "") {
            weatherType = getString(R.string.sunny)
        }

        moodWeatherPresenter.insertOrUpdateMoodWeatherOptions(
            emojiType,
            weatherType,
            tvDate.text.toString()
        )
    }
}

override fun getUpdateDatabaseCallback() {
    finish()
}

override fun onResume() {
    super.onResume()
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
    if(colorSelected!=0){
        btn_done.setBackgroundColor(colorSelected)
    } else {
        btn_done.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }
}

private fun addWeatherIcons() {
    myWeatherList.add(WeatherOptions(R.drawable.cloudy, getString(R.string.cloudy)))
    myWeatherList.add(WeatherOptions(R.drawable.rain, getString(R.string.rainy)))
    myWeatherList.add(WeatherOptions(R.drawable.snowy, getString(R.string.snowy)))
    myWeatherList.add(WeatherOptions(R.drawable.sunny, getString(R.string.sunny)))
    myWeatherList.add(WeatherOptions(R.drawable.thunder, getString(R.string.thunder)))
    myWeatherList.add(WeatherOptions(R.drawable.windy, getString(R.string.windy)))
}

private fun addMoodIcons() {
    myEmojiList.add(MoodOptions(R.drawable.angry, getString(R.string.angry)))
    myEmojiList.add(MoodOptions(R.drawable.cool, getString(R.string.cool)))
    myEmojiList.add(MoodOptions(R.drawable.happy, getString(R.string.happy)))
    myEmojiList.add(MoodOptions(R.drawable.neutral, getString(R.string.neutral)))
    myEmojiList.add(MoodOptions(R.drawable.sad, getString(R.string.sad)))
    myEmojiList.add(MoodOptions(R.drawable.boring, getString(R.string.boring)))
}

override fun handleKeyboard(): View {
    return weatherParent
}
}

