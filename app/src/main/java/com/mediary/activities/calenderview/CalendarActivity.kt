package com.mediary.activities.calenderview


import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.activities.calenderview.adapter.CalendarAdapter
import com.mediary.activities.calenderview.gesture.SwipeTouchListener
import com.mediary.activities.calenderview.pojo.CalendarIcon
import com.mediary.activities.calenderview.presenter.CalenderPresenterImpl
import com.mediary.activities.calenderview.view.CalenderActivityView
import com.mediary.base.BaseActivity
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.activity_edit_questions.*
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList


class CalendarActivity : BaseActivity(), View.OnClickListener, CalenderActivityView {
    private var currentMonth: String? = null
    private var imgLeft: ImageView? = null
    private var imgRight: ImageView? = null
    private var year: Int = 0
    private var month: Int = 0
    private var daysInMonth: Int = 0
    private var mFirstDayOfWeek: Int = 0
    private var currMonth: Int = 0
    private var currYear: Int = 0
    private var currDate: Int = 0
    private var iMonth: Int = 0
    private var isFirst: Boolean = true
    private var monthName: String? = null
    private lateinit var calenderPresenter: CalenderPresenterImpl
    private var dateOfTheWeek: String? = null
    private var yearToShow: String? = null

    private lateinit var calendarIconArrayList: ArrayList<CalendarIcon>

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_left -> {
                performLeftArrowFunctionality()
            }
            R.id.img_right -> {
                if (txt_month.text.toString() == currentMonth && txt_year.text.toString() == year.toString()) {
                    img_right.visibility = View.GONE
                } else {
                    img_right.visibility = View.VISIBLE

                }
                //to add
                if (year == currYear) {
                    if (month != currMonth) {
                        performRightArrowFuctionality()
                    }
                } else {
                    performRightArrowFuctionality()
                }
            }
            R.id.tvCalenderDay -> {
                val calendar = Calendar.getInstance()
                val month1 = calendar.get(Calendar.MONTH)
                month = month1
                val year1 = calendar.get(Calendar.YEAR)
                year = year1
                setMonthYear()
                isFirst = false

                if (calendarIconArrayList.size > 0) {
                    val cal = Calendar.getInstance()
                    cal.time = calendarIconArrayList[calendarIconArrayList.size - 1].calendarDate
                    val iconListMonth = cal.get(Calendar.MONTH)
                    val iconListYear = cal.get(Calendar.YEAR)

                    if (!(iconListMonth <= month && iconListYear <= year)
                    ) {
                        imgRight?.visibility = View.VISIBLE
                    } else {
                        imgRight?.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun performLeftArrowFunctionality() {
        if (month > 0) {
            month -= 1
        } else {
            month = 12
            month -= 1
            year -= 1
        }
        setMonthYear()
        isFirst = false

        if (calendarIconArrayList.size > 0) {
            val cal = Calendar.getInstance()
            cal.time = calendarIconArrayList[calendarIconArrayList.size - 1].calendarDate
            val iconListMonth = cal.get(Calendar.MONTH)
            val iconListYear = cal.get(Calendar.YEAR)

            if (!(iconListMonth <= month && iconListYear <= year)
            ) {
                imgRight?.visibility = View.VISIBLE
            } else {
                imgRight?.visibility = View.GONE
            }
        }
        val date = Date()
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        currentMonth = monthFormat.format(date)
        if (txt_month.text.toString() == currentMonth && txt_year.text.toString() == year.toString()) {
            img_right.visibility = View.GONE
        } else {
            img_right.visibility = View.VISIBLE

        }
    }

    fun performRightArrowFuctionality() {
        imgLeft?.visibility = View.VISIBLE
        if (month == 11) {
            month = 0
            year += 1
            isFirst = false
        } else {
            month += 1
            isFirst = false
        }
        setMonthYear()

        if (calendarIconArrayList.size > 0) {
            val cal = Calendar.getInstance()
            cal.time = calendarIconArrayList[calendarIconArrayList.size - 1].calendarDate
            imgRight?.visibility = View.VISIBLE

        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_calendar_view
    }

    private fun setMonthYear() {
        if (isFirst) {
            val calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            currDate = calendar.get(Calendar.DATE)
            currMonth = month
            currYear = year
        }
        monthName = getMonth(month)
        txt_month?.text = monthName
        txt_year?.text = year.toString()
        tvDate.text = year.toString()
        getMonthDays()
    }

    private fun getMonthDays() {
        if (month == currMonth && year == currYear) {
            imgLeft?.visibility = View.INVISIBLE
        }
        val iYear = year
        iMonth = if (month == currMonth) {
            Calendar.MONTH
        } else {
            month
        }
        val iDay = 1

        // Create a calendar object and set year and month
        val myCal = GregorianCalendar(iYear, iMonth, iDay)

        // Get the number of days in that month
        if (iMonth == 2 && monthName.equals("February")) {
            // Get the number of days in that month
            val yearMonthObject = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                YearMonth.of(iYear, 2)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                daysInMonth = yearMonthObject.lengthOfMonth()
            }
        } else {
            daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        val c = Calendar.getInstance()
        monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        c.set(year, month, 1, 0, 1, 1)
        mFirstDayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        getCalendar()
    }

    private fun getCalendar() {
        val adapter = CalendarAdapter(
            this,
            month,
            daysInMonth,
            mFirstDayOfWeek,
            currDate,
            year,
            calendarIconArrayList,
            this
        )
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        linearLayout.reverseLayout = false
        rv_calendar?.layoutManager = linearLayout
        rv_calendar?.adapter = adapter
    }

    private fun getMonth(month: Int): String {
        return DateFormatSymbols().months[month]
    }

    override fun setupUI() {
        rv_calendar.setOnTouchListener(object : SwipeTouchListener(this@CalendarActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                if (year == currYear) {
                    if (month != currMonth) {
                        performRightArrowFuctionality()
                    }
                } else {
                    performRightArrowFuctionality()
                }
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                performLeftArrowFunctionality()
            }
        })
        if (database != null) {
            calenderPresenter = CalenderPresenterImpl(this, database!!)
        }
        calendarIconArrayList = ArrayList()
        img_left.setOnClickListener(this)
        img_right.setOnClickListener(this)
        calenderPresenter.setIconsOnCalender(this@CalendarActivity, calendarIconArrayList)
        setMonthYear()

        val date = Date()
        setDayDate(date)
            val colorSelected =
                PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            tvCalenderDay.setTextColor(colorSelected)
            txt_year.setTextColor(colorSelected)
            tvSepratedComma.setTextColor(colorSelected)
            txt_month.setTextColor(colorSelected)
        }
        tvCalenderDay.text = getString(R.string.today)
        back.setOnClickListener {
            finish()
        }
        calenderPresenter.setMoodOptionWeatherOption()
        tvCalenderDay.setOnClickListener(this)
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        currentMonth = monthFormat.format(date)

    }

    override fun getIconsData(list: ArrayList<CalendarIcon>) {
        calendarIconArrayList = list
    }

    private fun setDayDate(date: Date) {
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        dateOfTheWeek = dateFormat.format(date)
        tvCalenderDate.text = dateOfTheWeek
        tvCalenderDate.typeface = Typeface.DEFAULT_BOLD
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayOfTheWeek = dayFormat.format(date)
        tvBottomDay.text = dayOfTheWeek
        val monthFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
        yearToShow = monthFormat.format(date)
        tvBottomYear.text = yearToShow
    }

    override fun handleKeyboard(): View {
        return calendarParent
    }

    override fun getDaysMoodOptions(isTrue: Boolean, emoji: String, weather: String) {
        if (emoji == "Happy") {
            imageMood.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.happy))
            if (isTrue) {
                imageMood.setColorFilter(
                    ContextCompat.getColor(this@CalendarActivity, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        } else if (emoji == "Cool") {
            imageMood.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cool))
        } else if (emoji == "Neutral") {
            imageMood.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.neutral))
        } else if (emoji == "Sad") {
            imageMood.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sad))
        } else if (emoji == "Boring") {
            imageMood.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.boring))
        } else if (emoji == "Angry") {
            imageMood.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.angry))
        }

        if (weather == "Cloudy") {
            imageWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cloudy))
        } else if (weather == "Rainy") {
            imageWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rain))
        } else if (weather == "Snowy") {
            imageWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.snowy))
        } else if (weather == "Sunny") {
            imageWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sunny))
            if (isTrue) {
                imageMood.setColorFilter(
                    ContextCompat.getColor(this@CalendarActivity, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        } else if (weather == "Thunder") {
            imageWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.thunder))
        } else if (weather == "Windy") {
            imageWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.windy))
        }
    }

    override fun onResume() {
        val date = Date()
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        currentMonth = monthFormat.format(date)
        if (txt_month.text.toString() == currentMonth && txt_year.text.toString() == year.toString()) {
            img_right.visibility = View.GONE
        } else {
            img_right.visibility = View.VISIBLE

        }
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            tvCalenderDay.setTextColor(colorSelected)
            txt_year.setTextColor(colorSelected)
            txt_month.setTextColor(colorSelected)
            tvSepratedComma.setTextColor(colorSelected)
        }
        super.onResume()
    }
}