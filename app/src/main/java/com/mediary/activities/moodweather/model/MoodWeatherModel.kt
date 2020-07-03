package com.mediary.activities.moodweather.model

import com.mediary.activities.moodweather.presenter.MoodWeatherPresenter
import com.mediary.database.AppDatabase
import com.mediary.database.entity.WeatherMoodTable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MoodWeatherModel(
    private var moodWeatherPresenter: MoodWeatherPresenter,
    private var database: AppDatabase
) {

    /**
     * insert and update weather and emoji data
     */
    fun insertUpdateData(emojiType: String, weatherType: String,currentDate:String) {
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val inputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.US)

        val date = inputFormat.parse(currentDate)
        val outputText = outputFormat.format(date)

        val list: List<WeatherMoodTable> = database.weatherMoodDao().getMoodData()
        var isDateExist = false
        for (item: WeatherMoodTable in list) {
            if (item.date == outputText) {
                isDateExist = true
            }
        }
        if (isDateExist) {
            database.weatherMoodDao().updateMoodOption(emojiType, "MOOD", outputText)
            database.weatherMoodDao().updateMoodOption(weatherType, "WEATHER", outputText)
        } else {
            val moodOptions = WeatherMoodTable(emojiType, "MOOD", outputText)
            database.weatherMoodDao().insertWeatherOption(moodOptions)

            val weatherOptions = WeatherMoodTable(weatherType, "WEATHER", outputText)
            database.weatherMoodDao().insertWeatherOption(weatherOptions)
        }
        moodWeatherPresenter.getUpdateCallback()

    }
}