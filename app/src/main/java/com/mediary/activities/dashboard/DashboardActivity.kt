package com.mediary.activities.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget
import com.google.android.material.tabs.TabLayout
import com.mediary.R
import com.mediary.activities.ColorChooseActivity
import com.mediary.activities.PromptLibraryActivity
import com.mediary.activities.answerstoquestions.QuestionAnswerActivity
import com.mediary.activities.calenderview.CalendarActivity
import com.mediary.activities.calenderview.gesture.SwipeTouchListener
import com.mediary.activities.dashboard.presenter.DashboardPresenter
import com.mediary.activities.dashboard.presenter.DashboardPresenterImpl
import com.mediary.activities.dashboard.view.DashboardView
import com.mediary.activities.editquestions.EditQuestionActivity
import com.mediary.activities.moodweather.MoodWeatherLogActivity
import com.mediary.activities.questionswithanswersscreen.view.QuestionsWithAnswersActivity
import com.mediary.activities.settings.SettingsScreen
import com.mediary.adapters.DashboardGridAdapter
import com.mediary.base.BaseActivity
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions
import com.mediary.listeners.OnCustomizeColorClickListener
import com.mediary.listeners.OnDeleteDailyDataClickListener
import com.mediary.listeners.OnGridItemClickListener
import com.mediary.receiver.CheckRecentRun
import com.mediary.receiver.MyReceiver
import com.mediary.utils.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DashboardActivity : BaseActivity(), Toolbar.OnMenuItemClickListener, OnGridItemClickListener,
    DashboardView, OnDeleteDailyDataClickListener, View.OnClickListener,
    OnCustomizeColorClickListener {
    private var intent_from: String = ""
    private lateinit var questionsData: Questions
    private lateinit var imagesList: List<MeDairyImagesEntity>
    private var isAlertDialogClicked: Boolean = false
    private lateinit var currentDateNew: Date
    private val NOTIFICATION_REMINDER_NIGHT = 2
    private var item1: View? = null
    private var recyclerAdapter: DashboardGridAdapter? = null
    private var dateString: String = ""
    private var strDate: String = ""
    private lateinit var dateMills: Date
    private lateinit var questionDate: PromptQuestion
    private var millis: Long = 0
    private var countHint: Int = 0
    private var year: String? = null
    private var dateOfTheWeek: String? = null
    private var mQuestionAnswerList: MutableList<Questions> = mutableListOf()
    private lateinit var tabOne: AppCompatImageView
    private lateinit var tabTWo: AppCompatImageView
    private lateinit var tabThree: AppCompatImageView
    private lateinit var tabFour: AppCompatImageView
    private lateinit var tabFive: AppCompatImageView
    private lateinit var tabSix: AppCompatImageView
    private val DAY_IN_MILISECOND = 24 * 60 * 60 * 1000
    private lateinit var insertedDate: Date
    private lateinit var futureDate: Date
    private lateinit var dashboardPresenter: DashboardPresenter
    private val TAG = "MainActivity"
    private var settings: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var menu: Menu? = null
    private var isMoodWeatherSet: Boolean = false
    var emoji: String = ""
    var weather: String = ""
    private var flagDecoration: Boolean = false
    private lateinit var target1: SimpleTarget
    private lateinit var target2: SimpleTarget
    private lateinit var target4: SimpleTarget
    private lateinit var target5: SimpleTarget
    private lateinit var target3: SimpleTarget

    override fun getLayout(): Int {
        return R.layout.activity_dashboard
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        this.menu = menu
        val sdf = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
        val currentDate = sdf.format(Date())
        setMoodWeatherData(currentDate)
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dateLinearLayout -> {
                val i = Intent(this@DashboardActivity, CalendarActivity::class.java)
                startActivityForResult(i, 1)
            }
        }
    }

    override fun showMoodWeatherData(emoji: String, weather: String) {
        val isMoodWeatherOptionsNotSelected = false
        this.emoji = emoji
        this.weather = weather
        if (emoji == getString(R.string.happy)) {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.happy)
            if (!isMoodWeatherOptionsNotSelected) {
                menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.colorPrimary
                    ), PorterDuff.Mode.SRC_IN
                ))

            } else {
                menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                ))
            }
        } else if (emoji == getString(R.string.cool)) {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.cool)
            menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (emoji == getString(R.string.neutral)) {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.neutral)
            menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))

        } else if (emoji == getString(R.string.sad)) {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.sad)
            menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (emoji == getString(R.string.boring)) {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.boring)
            menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (emoji == getString(R.string.angry)) {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.angry)
            menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else {
            menu?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.happy)
            menu?.getItem(1)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.black
                ), PorterDuff.Mode.SRC_IN
            ))
        }

        if (weather == getString(R.string.cloudy)) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.cloudy)
            menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (weather == getString(R.string.rainy)) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.rain)
            menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (weather == getString(R.string.snowy)) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.snowy)
            menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (weather == getString(R.string.sunny)) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.sunny)
            if (!isMoodWeatherOptionsNotSelected) {
                menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.colorPrimary
                    ), PorterDuff.Mode.SRC_IN
                ))
            } else {
                menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                ))
            }
        } else if (weather == getString(R.string.thunder)) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.thunder)
            menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else if (weather == getString(R.string.windy)) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.windy)
            menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            ))
        } else {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.sunny)
            menu?.getItem(2)?.icon?.colorFilter = (PorterDuffColorFilter(
                ContextCompat.getColor(
                    this@DashboardActivity,
                    R.color.black
                ), PorterDuff.Mode.SRC_IN
            ))
        }
    }

    private fun setMoodWeatherData(currentDate: String) {
        dashboardPresenter.getMoodWeatherOptions(currentDate)
    }

    override fun setupUI() {
       /* val extras = intent.getExtras()
        if (extras != null) {
            intent_from = extras.getString(Constants.INTENT_FROM)!!
        }*/


        var count = PreferenceHandler.readInteger(this, "AppOpenCount", 0)
        PreferenceHandler.writeInteger(this, "AppOpenCount", ++count)

        if (count != 0 && count % 5 == 0) {
            val alerts = Alert(this)
            alerts.showCustomizeColorAlert(this)
            //startActivity(Intent(this,ColorChooseActivity::class.java))
        }

        dashboardPresenter = DashboardPresenterImpl(this, database!!, mQuestionAnswerList)
        val isFromNotification = intent.getIntExtra("isFromNotification", 0)
        if (isFromNotification == 1) {
            addAnalytics("FromNotification")
        } else {
            addAnalytics("E:\\Android Workspace\\Me_Diary_With_New_Library\\Me_Diary_With_New_Library\\app\\src\\main\\java\\com\\mediary\\activities\\dashboard\\DashboardActivity.kt")
        }
        centeredToolbar.inflateMenu(R.menu.menu)
        setSupportActionBar(centeredToolbar)
        centeredToolbar.setOnMenuItemClickListener(this)
        dateLinearLayout.setOnClickListener(this)
        addPromptQuestions()
        questionDate = database!!.questionsDao().getQuestionsDate()
        val date = Date()
        setDayDate(date)
        millis = System.currentTimeMillis()
        dateMills = Date(System.currentTimeMillis())
        currentDateNew = Date(System.currentTimeMillis())
        setTabLayout()
        setTabIcons()
        detectGesture()
        insertedDate =
            Utilities.getFormattedDate(questionDate.insertedDate.time - DAY_IN_MILISECOND * 2)
        futureDate = Utilities.getFormattedDate(System.currentTimeMillis() + DAY_IN_MILISECOND * 2)
        setDailyAlarm()
        item1 = this.findViewById(R.id.smiley)
        if ((PreferenceHandler.readInteger(this@DashboardActivity, PreferenceHandler.IS_FIRST_TIME, 0) < 4)) {
            target1 = SimpleTarget.Builder(this)
                .setTarget(item1!!)
                .setHighlightRadius(10f)
                .setDescription("First record the day's weather and mood.\n This will help you to remember. Swipe down to see reading mode.")
                .setStartDelayMillis(500L)
                .setTitle("")
                .build()
            countHint += 1
            target3 = SimpleTarget.Builder(this)
                .setDescription(DataBaseHelper.getHintList()[countHint])
                .setTarget(returnHintView())
                .setTitle("")
                .setHighlightRadius(10f)
                .build()
            countHint += 1

            target4 = SimpleTarget.Builder(this)
                .setDescription(DataBaseHelper.getHintList()[countHint])
                .setTarget(returnHintView())
                .setHighlightRadius(10f)
                .setTitle("")
                .build()
            countHint += 1
            target5 = SimpleTarget.Builder(this)
                .setDescription(DataBaseHelper.getHintList()[countHint])
                .setTarget(returnHintView())
                .setHighlightRadius(10f)
                .setTitle("")
                .build()

            countHint += 1

            PreferenceHandler.writeInteger(
                this@DashboardActivity,
                PreferenceHandler.IS_FIRST_TIME,
                countHint
            )

            TargetInstructions.with(this@DashboardActivity)
                .setTargets(arrayListOf(target1, target3, target4, target5))
                .setFadeDuration(200L)
                .setFadeInterpolator(LinearOutSlowInInterpolator())
                .setOverlayColorResId(R.color.colorPrimary) // Background color
                .start()
        }

        settings = getSharedPreferences(PREFS, MODE_PRIVATE)
        editor = settings?.edit()
        if (!settings!!.contains("lastRun"))
            enableNotification()
        else
            recordRunTime()
        Log.v(TAG, "Starting CheckRecentRun service...")
        startService(Intent(this, CheckRecentRun::class.java))
    }

    private fun detectGesture() {
        centeredToolbar.setOnTouchListener(object : SwipeTouchListener(this@DashboardActivity) {
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                val i = Intent(this@DashboardActivity, QuestionsWithAnswersActivity::class.java)
                val dateFormat =
                    SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                val strDate = dateFormat.format(dateMills)
                i.putExtra(Constants.DISPLAYED_DATE, strDate)
                startActivity(i)
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
            }
        })
    }

    private fun recordRunTime() {
        editor!!.putLong("lastRun", System.currentTimeMillis())
        editor!!.commit()
    }

    private fun enableNotification() {
        editor!!.putLong("lastRun", System.currentTimeMillis())
        editor!!.putBoolean("enabled", true)
        editor!!.commit()
        Log.v(TAG, "Notifications enabled")
    }


    private fun setDailyAlarm() {
        val notifyIntent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this@DashboardActivity,
            NOTIFICATION_REMINDER_NIGHT,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmStartTime = Calendar.getInstance()
        val now = Calendar.getInstance()
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 6)
        alarmStartTime.set(Calendar.MINUTE, 0)
        alarmStartTime.set(Calendar.SECOND, 0)
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            1000 * 60,
            pendingIntent
        )
    }



    private fun addPromptQuestions() {
        dashboardPresenter.addPromptQuestions(DataBaseHelper.getQuestionsList(this))
    }

    override fun onItemClick(questionData: List<Questions>, position: Int, view: View) {
        val intent = Intent(this@DashboardActivity, QuestionAnswerActivity::class.java)
        intent.putExtra(Constants.POSITION, position)
        val dateFormat = SimpleDateFormat(getString(R.string.dd_mmm_yyyy), Locale.getDefault())
        strDate = dateFormat.format(dateMills)
        intent.putExtra(Constants.DATE, strDate)
        val b = Bundle()
        b.putSerializable(Constants.QUESTIONS_DATA, questionData as Serializable)
        intent.putExtras(b)
        startActivity(intent)
    }

    override fun showAddedAnswers(allQuestions: List<Questions>) {
        this.mQuestionAnswerList = allQuestions as MutableList<Questions>
        refreshContent()
    }

    private fun setTabLayout() {
        tabs.addTab(tabs.newTab().setText("calender"))
        tabs.addTab(tabs.newTab().setText("edit"))
        tabs.addTab(tabs.newTab().setText("question"))
        tabs.addTab(tabs.newTab().setText("back"))
        tabs.addTab(tabs.newTab().setText("front"))
        tabs.addTab(tabs.newTab().setText("delete"))
        tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                performOnTabClick(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //not in my use
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                performOnTabClick(tab.position)
            }
        })
    }

    private fun returnHintView(): View {
        var viewHint: View? = null
        if (PreferenceHandler.readInteger(
                this@DashboardActivity,
                PreferenceHandler.IS_FIRST_TIME,
                countHint
            ) < 4
        ) {
            when {
                PreferenceHandler.readInteger(
                    this@DashboardActivity,
                    PreferenceHandler.IS_FIRST_TIME,
                    countHint
                ) == 0 -> {
                    val layoutManager = GridLayoutManager(this@DashboardActivity, 2)
                    dashboardRV.layoutManager = layoutManager
                    layoutManager.findViewByPosition(3)
                    viewHint = dashboardRV
                }
                PreferenceHandler.readInteger(
                    this@DashboardActivity,
                    PreferenceHandler.IS_FIRST_TIME,
                    countHint
                ) == 1 -> viewHint = tabOne
                PreferenceHandler.readInteger(
                    this@DashboardActivity,
                    PreferenceHandler.IS_FIRST_TIME,
                    countHint
                ) == 2 -> viewHint = tabTWo
                PreferenceHandler.readInteger(
                    this@DashboardActivity,
                    PreferenceHandler.IS_FIRST_TIME,
                    countHint
                ) == 3 -> viewHint = tabThree
                else -> viewHint = item1
            }
        }
        return viewHint!!
    }

    private fun performOnTabClick(position: Int) {
        when (position) {
            0 -> {
                if (PreferenceHandler.readInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    ) < 4
                ) {
                    showSuggestions(
                        returnHintView(),
                        DataBaseHelper.getHintList()[PreferenceHandler.readInteger(
                            this@DashboardActivity,
                            PreferenceHandler.IS_FIRST_TIME,
                            countHint
                        )]
                    )
                    countHint += 1
                    PreferenceHandler.writeInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    )
                } else {
                    val i = Intent(this@DashboardActivity, CalendarActivity::class.java)
                    startActivityForResult(i, 1)
                }
            }

            1 -> {
                if (PreferenceHandler.readInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    ) < 4
                ) {
                    showSuggestions(
                        returnHintView(),
                        DataBaseHelper.getHintList()[PreferenceHandler.readInteger(
                            this@DashboardActivity,
                            PreferenceHandler.IS_FIRST_TIME,
                            countHint
                        )]
                    )
                    countHint += 1
                    PreferenceHandler.writeInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    )
                } else {
                    val intent = Intent(this@DashboardActivity, EditQuestionActivity::class.java)
                    intent.putExtra(Constants.DATE, strDate)
                    startActivity(intent)
                }
            }
            2 -> {
                if (PreferenceHandler.readInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    ) < 4
                ) {
                    showSuggestions(
                        returnHintView(),
                        DataBaseHelper.getHintList()[PreferenceHandler.readInteger(
                            this@DashboardActivity,
                            PreferenceHandler.IS_FIRST_TIME,
                            countHint
                        )]
                    )
                    countHint += 1
                    PreferenceHandler.writeInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    )
                } else {
                    val intent = Intent(this@DashboardActivity, PromptLibraryActivity::class.java)
                    intent.putExtra(Constants.DATE, strDate)
                    startActivity(intent)
                    //  var fragment = ColorChooserFragment.newInstance()
                }
            }
            3 -> {
                if (PreferenceHandler.readInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    ) < 4
                ) {
                    showSuggestions(
                        returnHintView(),
                        DataBaseHelper.getHintList()[PreferenceHandler.readInteger(
                            this@DashboardActivity,
                            PreferenceHandler.IS_FIRST_TIME,
                            countHint
                        )]
                    )
                    countHint += 1
                    PreferenceHandler.writeInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    )
                } else {
                    dateMills = Date(dateMills.time - 1 * 24 * 3600 * 1000)
                    addQuestions(dateMills)
                    setDayDate(dateMills)
                    val sdf = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                    val currentDate = sdf.format(dateMills)
                    setMoodWeatherData(currentDate)
                }
            }
            4 -> {
                if (PreferenceHandler.readInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    ) < 4
                ) {
                    showSuggestions(
                        returnHintView(),
                        DataBaseHelper.getHintList()[PreferenceHandler.readInteger(
                            this@DashboardActivity,
                            PreferenceHandler.IS_FIRST_TIME,
                            countHint
                        )]
                    )
                    countHint += 1
                    PreferenceHandler.writeInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    )
                } else {
                    val sdf = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                    val selectedDate = sdf.format(dateMills)
                    val sdfCurrent =
                        SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                    val currentDate = sdfCurrent.format(currentDateNew)

                    if (selectedDate < currentDate) {
                        dateMills = Date(dateMills.time + 1 * 24 * 3600 * 1000)
                        addQuestions(dateMills)
                        setDayDate(dateMills)
                        val dateFormat =
                            SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                        val curDate = dateFormat.format(dateMills)
                        setMoodWeatherData(curDate)
                    }
                }
            }
            5 -> {
                isAlertDialogClicked = true
                if (PreferenceHandler.readInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    ) < 4
                ) {
                    showSuggestions(
                        returnHintView(),
                        DataBaseHelper.getHintList()[PreferenceHandler.readInteger(
                            this@DashboardActivity,
                            PreferenceHandler.IS_FIRST_TIME,
                            countHint
                        )]
                    )
                    countHint += 1
                    PreferenceHandler.writeInteger(
                        this@DashboardActivity,
                        PreferenceHandler.IS_FIRST_TIME,
                        countHint
                    )
                } else {
                    isDialogOpened = false
                    if (database != null) {
                        for (i in database!!.questionsDao().getAllQuestionsAccordingToDate(
                            dateString
                        ).indices) {
                            questionsData =
                                database!!.questionsDao().getAllQuestionsAccordingToDate(dateString)[i]
                            imagesList = database!!.imagesDao()
                                .setImages(questionsData.uid, questionsData.joined_date)
                            if (questionsData.answers.isNotEmpty() || imagesList.isNotEmpty() || emoji != "null" || weather != "null") {
                                if (questionsData.isSelected && !isDialogOpened) {
                                    val alerts = Alert(this@DashboardActivity)
                                    alerts.showDailyDeleteDataAlert(this, dateString)
                                    isDialogOpened = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private var isDialogOpened = false
    override fun onDeleteDataClick(date: String) {
        database?.questionsDao()?.updateDailyData("", dateString, "")
        database?.imagesDao()?.deleteImages(dateString)
        val sdf = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
        val currentDate = sdf.format(dateMills)
        database?.weatherMoodDao()?.deleteEmojis(currentDate)
        setMoodWeatherData(currentDate)
        mQuestionAnswerList = database?.questionsDao()?.getAllQuestions(
            dateString, hiddenData = false, selectedData = true, isDeleted = false
        ) as MutableList<Questions>
        recyclerAdapter?.setList(mQuestionAnswerList)
    }

    @SuppressLint("InflateParams")
    private fun setTabIcons() {
        tabOne = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabOne.setImageResource(R.drawable.calendar)
        tabs.getTabAt(0)?.customView = tabOne
        tabTWo = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabTWo.setImageResource(R.drawable.pencil)
        tabs.getTabAt(1)?.customView = tabTWo
        tabThree = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabThree.setImageResource(R.drawable.questions)
        tabs.getTabAt(2)?.customView = tabThree
        tabFour = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabFour.setImageResource(R.drawable.back_arrow)
        tabs.getTabAt(3)?.customView = tabFour
        tabFive = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabFive.setImageResource(R.drawable.front_arrow)
        tabs.getTabAt(4)?.customView = tabFive
        tabSix = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabSix.setImageResource(R.drawable.delete)
        tabs.getTabAt(5)?.customView = tabSix
    }

    private fun addQuestions(dateMills: Date) {
        val dateFormat = SimpleDateFormat(getString(R.string.dd_mmm_yyyy), Locale.getDefault())
        strDate = dateFormat.format(dateMills)
        dateString = Utilities.dbDateFormat(strDate)
        mQuestionAnswerList = dashboardPresenter.addQuestions(
            dateString,
            hiddenData = true,
            selectedData = false
        ) as MutableList<Questions>
    }

    private fun setDayDate(date: Date) {
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        dateOfTheWeek = dateFormat.format(date)
        tvDateDashboard.text = dateOfTheWeek
        tvDateDashboard.typeface = Typeface.DEFAULT_BOLD
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayOfTheWeek = dayFormat.format(date)
        tvDay.text = dayOfTheWeek
        val monthFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
        year = monthFormat.format(date)
        tvYear.text = year
    }

    override fun handleKeyboard(): View {
        return dashParentLayout
    }

    private fun showSuggestions(view: View, title: String) {
        target2 = SimpleTarget.Builder(this)
            .setDescription(title)
            .setTarget(view)
            .setTitle(getString(R.string.floating_button))
            .setHighlightRadius(100f) // Circle shape
            .setDescription(getString(R.string.floating_action_button))
            .build()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.settings) {
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }
        val sdf = SimpleDateFormat(getString(R.string.dd_mmm_yyyy), Locale.getDefault())
        val currentDate = sdf.format(dateMills)
        val intent = Intent(this, MoodWeatherLogActivity::class.java)
        intent.putExtra(getString(R.string.mood), emoji)
        intent.putExtra(getString(R.string.weather), weather)
        intent.putExtra(getString(R.string.current_date), currentDate)
        if (item?.itemId == R.id.smiley) {
            startActivity(intent)
        }
        if (item?.itemId == R.id.weather) {
            startActivity(intent)
        }
        return false
    }


    override fun onResume() {
        super.onResume()

        val layoutManager = GridLayoutManager(this@DashboardActivity, 2)
        dashboardRV.layoutManager = layoutManager
        val sdf = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
        val currentDate = sdf.format(dateMills)
        val currentDate2 = sdf.format(Date())
        if (!isMoodWeatherSet) {
            addQuestions(dateMills)
            setMoodWeatherData(currentDate)
            if (currentDate == currentDate2) {
                setMoodWeatherData(currentDate2)
            } else {
                setMoodWeatherData(currentDate)
            }
        }
        isMoodWeatherSet = false
    }


    companion object {
        const val PREFS = "PrefsFile"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                isMoodWeatherSet = true
                val result: String? = data?.getStringExtra(getString(R.string.selected_date))
                val sdf = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                var date: Date? = null
                val format = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
                try {
                    date = format.parse(result!!)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val currentDate = sdf.format(date!!)
                setMoodWeatherData(currentDate)
                dateMills = date
                val dateFormat =
                    SimpleDateFormat(getString(R.string.dd_mmm_yyyy), Locale.getDefault())
                strDate = dateFormat.format(dateMills)
                dateString = Utilities.dbDateFormat(strDate)
                mQuestionAnswerList = dashboardPresenter.addQuestions(
                    dateString,
                    hiddenData = true,
                    selectedData = false
                ) as MutableList<Questions>
                setDayDate(dateMills)
                insertedDate = Utilities.getFormattedDate(dateMills.time)
            }

        }
    }

    private fun refreshContent() {
        if (database != null) {
            if (mQuestionAnswerList.size < 1 || mQuestionAnswerList.size == 0) {
                val list = database!!.questionsDao().getAllQuestionsAccordingToDate(dateString)
                if (list.isNotEmpty()) {
                    val question = list[0]
                    mQuestionAnswerList.add(question)
                }
                if (recyclerAdapter == null) {
                    recyclerAdapter = DashboardGridAdapter(
                        mQuestionAnswerList,
                        this,
                        database!!,
                        this@DashboardActivity
                    )
                    dashboardRV.adapter = recyclerAdapter
                } else {
                    recyclerAdapter!!.setList(mQuestionAnswerList)
                }
                if (mQuestionAnswerList.size == 1) {
                    if (!flagDecoration) {
                        dashboardRV.addItemDecoration(
                            DividerItemDecoration(
                                this,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }
                }
            } else {
                if (!flagDecoration) {
                    dashboardRV.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    dashboardRV.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.HORIZONTAL
                        )
                    )
                    flagDecoration = true
                }


                if (recyclerAdapter == null) {
                    recyclerAdapter = DashboardGridAdapter(
                        mQuestionAnswerList,
                        this,
                        database!!,
                        this@DashboardActivity
                    )
                    dashboardRV.adapter = recyclerAdapter
                } else {
                    recyclerAdapter!!.setList(mQuestionAnswerList)
                }
            }
        }
    }

    override fun onColorChangeClick() {
        startActivity(Intent(this, ColorChooseActivity::class.java))
    }

}