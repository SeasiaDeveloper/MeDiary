package com.mediary.activities.calenderview.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.mediary.R
import com.mediary.activities.calenderview.pojo.CalendarIcon
import com.mediary.activities.calenderview.presenter.CalenderPresenter
import com.mediary.database.AppDatabase
import com.mediary.database.entity.WeatherMoodTable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalenderModel(
    private var calenderPresenter: CalenderPresenter,
    private var database: AppDatabase
) {

    private  var weather: String?=null
    private var emoji:String?=null

    /**
     * set mood and weather options of current day from database
     */
    fun getMoodWeatherOptions() {
        var isMoodWeatherOptionsNotSelected = false
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        emoji = database.weatherMoodDao().getMoodTypeByDate(currentDate, "MOOD")
        weather = database.weatherMoodDao().getMoodTypeByDate(currentDate, "WEATHER")
        if (emoji == null) {
            emoji = "Happy"
            isMoodWeatherOptionsNotSelected = true
        }

        if (weather == null) {
            weather = "Sunny"
            isMoodWeatherOptionsNotSelected = true
        }
        calenderPresenter.getMoodOptions(isMoodWeatherOptionsNotSelected, emoji!!, weather!!)
    }

    /**
     * get mood options of days and set it on Calender
     */

    fun getIconsToSetOnCalender(context: Context,calenderIconList: ArrayList<CalendarIcon>) {
        val sdf = SimpleDateFormat(context.getString(R.string.dd_mm_yyyy), Locale.getDefault())
        val currentDate = sdf.format(Date())
        val list: List<WeatherMoodTable> = database.weatherMoodDao().getMoodType("MOOD")
        emoji = database.weatherMoodDao().getMoodTypeByDate(currentDate, "MOOD")
        weather = database.weatherMoodDao().getMoodTypeByDate(currentDate, "WEATHER")
        if (emoji == null) {
            emoji = "Happy"
        }

        if (weather == null) {
            weather = "Sunny"
        }

        var monthInInt: Int = -1
        var dateInInt: Int = -1
        var yearInInt: Int = -1

        for (item: WeatherMoodTable in list) {
            var date: Date
            val format = SimpleDateFormat(context.getString(R.string.dd_mm_yyyy),Locale.getDefault())
            try {
                date = format.parse(item.date)
                val formatted = SimpleDateFormat("MM",Locale.getDefault()).format(date)
                monthInInt = Integer.parseInt(formatted) - 1
                val formatted2 = SimpleDateFormat("dd",Locale.getDefault()).format(date)
                dateInInt = Integer.parseInt(formatted2)
                val formatted3 = SimpleDateFormat("yyyy",Locale.getDefault()).format(date)
                yearInInt = Integer.parseInt(formatted3)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val calendarIcon1: CalendarIcon

            if (item.type == "Happy") {
                calendarIcon1 = CalendarIcon()
               val date3 = Date()
                date3.year = yearInInt
                date3.month = monthInInt
                date3.date = dateInInt
                calendarIcon1.calendarDate = date3
                calendarIcon1.iconPath = ContextCompat.getDrawable(context, R.drawable.happy)
                calenderIconList.add(calendarIcon1)
            } else if (item.type == "Cool") {
                calendarIcon1 = CalendarIcon()
                val date3 = Date()
                date3.year = yearInInt
                date3.month = monthInInt
                date3.date = dateInInt
                calendarIcon1.calendarDate = date3
                calendarIcon1.iconPath = ContextCompat.getDrawable(context, R.drawable.cool)
                calenderIconList.add(calendarIcon1)
            } else if (item.type == "Neutral") {
                calendarIcon1 = CalendarIcon()
                val date3 = Date()
                date3.year = yearInInt
                date3.month = monthInInt
                date3.date = dateInInt
                calendarIcon1.calendarDate = date3
                calendarIcon1.iconPath = ContextCompat.getDrawable(context, R.drawable.neutral)
                calenderIconList.add(calendarIcon1)
            } else if (item.type == "Sad") {
                calendarIcon1 = CalendarIcon()
                val date3 = Date()
                date3.year = yearInInt
                date3.month = monthInInt
                date3.date = dateInInt
                calendarIcon1.calendarDate = date3
                calendarIcon1.iconPath = ContextCompat.getDrawable(context, R.drawable.sad)
                calenderIconList.add(calendarIcon1)
            } else if (item.type == "Boring") {
                calendarIcon1 = CalendarIcon()
                val date3 = Date()
                date3.year = yearInInt
                date3.month = monthInInt
                date3.date = dateInInt
                calendarIcon1.calendarDate = date3
                calendarIcon1.iconPath = ContextCompat.getDrawable(context, R.drawable.boring)
                calenderIconList.add(calendarIcon1)
            } else if (item.type == "Angry") {
                calendarIcon1 = CalendarIcon()
                val date3 = Date()
                date3.year = yearInInt
                date3.month = monthInInt
                date3.date = dateInInt
                calendarIcon1.calendarDate = date3
                calendarIcon1.iconPath = ContextCompat.getDrawable(context, R.drawable.angry)
                calenderIconList.add(calendarIcon1)
            }
        }

        calenderPresenter.getIconsToSetOnCalender(calenderIconList)
    }
}