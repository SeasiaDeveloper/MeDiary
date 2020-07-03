package com.mediary.activities.calenderview.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.activities.calenderview.CalendarActivity
import com.mediary.activities.calenderview.pojo.CalendarIcon
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.activity_edit_questions.*
import java.util.*


class CalendarAdapter(
    private val mContext: Context,
    private val month: Int,
    private val days: Int,
    private val mFirstDayOfWeek: Int,
    private val currDate: Int,
    private val year: Int,
    internal var calendarIconArrayList: ArrayList<CalendarIcon>,
    private val mActivity: CalendarActivity
) : RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    private lateinit var selectedDate: String
    private val currMonth: Int
    private val currYear: Int
    private val mArrayDays = ArrayList<String>()
    private var date = 1
    private var count = 1
    private var isFirst: Boolean? = true
    private var isSecond: Boolean? = true
    private var isEmpty: Boolean = false
    private var mCountTextOccupied: Int = 0

    init {
        isEmpty = true
        val calendar = Calendar.getInstance()
        currMonth = calendar.get(Calendar.MONTH)
        currYear = calendar.get(Calendar.YEAR)
        addWeekDays()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_recyclerview, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //creating sunday,monday etc days:
        if (position == 0) {
            for (day in mArrayDays) {
                val textView = TextView(mContext)
                textView.layoutParams =
                    LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                textView.gravity = Gravity.CENTER
                textView.text = day
                    val colorSelected = PreferenceHandler.readInteger(mContext, PreferenceHandler.COLOR_SELECTION, 0)
                if (colorSelected != 0) {
                    textView.setTextColor(colorSelected)
                }else {
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                }
                textView.setTypeface(null, Typeface.BOLD)
                holder.mLayout.addView(textView)
            }
        } else {
            if (isEmpty) {
                isEmpty = false
                for (j in 1 until mFirstDayOfWeek) {
                    mCountTextOccupied = j
                    mCountTextOccupied = 7 - mCountTextOccupied
                    count++
                    val textView = TextView(mContext)
                    textView.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                    textView.gravity = Gravity.CENTER
                    textView.text = " "
                    holder.mLayout.addView(textView)
                }
            } else {
                mCountTextOccupied = 0
            }
            //to continue date=1 on position=1
            if (isFirst!!) {
                isFirst = false
            } else {//to increment date from position=2 onwards acc to dates in position=1
                date = date - count + 1
            }

            //to print dates from 1 to total no of days
            for (i in date until date + 7) {
                //old one
                val subParent = LinearLayout(mContext)
                subParent.layoutParams =
                    LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
                subParent.orientation = LinearLayout.VERTICAL
                subParent.gravity = Gravity.CENTER
                subParent.weightSum = 2f
                subParent.setPadding(0, 10, 0, 10)

                val textView = TextView(mContext)
                val rPrams = RelativeLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                rPrams.setMargins(5, 5, 5, 5)
                textView.layoutParams = rPrams
                val imgIcon = ImageView(mContext)
                imgIcon.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

                if (i <= days) {
                    if (i == currDate && month == currMonth && year == currYear) {
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        textView.setTypeface(null, Typeface.BOLD)
                    }
                    if (mCountTextOccupied != 0 && mCountTextOccupied >= i) {
                        textView.text = "$i"
                    } else if (mCountTextOccupied == 0) {
                        textView.text = "$i"
                    }
                    for (iconPos in 0 until calendarIconArrayList.size) {
                        if (calendarIconArrayList[iconPos].calendarDate?.date == i && calendarIconArrayList[iconPos].calendarDate?.month == month && calendarIconArrayList[iconPos].calendarDate?.year == year) {
                            imgIcon.setImageDrawable(calendarIconArrayList[iconPos].iconPath)
                            val colorSelected = PreferenceHandler.readInteger(mContext, PreferenceHandler.COLOR_SELECTION, 0)
                            if (colorSelected != 0) {
                                imgIcon.setColorFilter(colorSelected, PorterDuff.Mode.SRC_IN)                            }else {
                                imgIcon.setColorFilter(
                                    ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)                            }
                            imgIcon.layoutParams.height = 50
                            imgIcon.layoutParams.width = 50
                            subParent.background = ContextCompat.getDrawable(
                                mContext,
                                R.drawable.white_circulat_drawable
                            )
                        }
                    }
                    subParent.addView(imgIcon)
                    subParent.addView(textView)
                    holder.mLayout.addView(subParent)
                }

                textView.setOnClickListener {
                    selectedDate = textView.text.toString()
                    if (year == currYear && month == currMonth) {
                        if (currDate > selectedDate.toInt() || currDate == selectedDate.toInt())
                            finishActivity(i)
                    }else{
                        finishActivity(i)
                    }
                }

                imgIcon.setOnClickListener {
                    val text1 = textView.text.toString()
                    if (year == currYear && month == currMonth) {
                        if (currDate > text1.toInt() || currDate == text1.toInt())
                            finishActivity(i)
                    }else{
                        finishActivity(i)
                    }
                }

            }
            mCountTextOccupied = 0
            if (isSecond!!) {
                date += 7
                isSecond = false
            } else {
                date = date + 7 + count - 1
            }//after position=2 increment dates acc to count and previous date
        }
    }

    fun finishActivity(i: Int) {
        val returnIntent = Intent()
        var date = ""
        date = i.toString() + "/" + (month + 1) + "/" + year
        returnIntent.putExtra("SELECTEDDATE", date)
        mActivity.setResult(Activity.RESULT_OK, returnIntent)
        mActivity.finish()
    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mLayout: LinearLayout = itemView.findViewById<View>(R.id.layout_main) as LinearLayout

    }

    private fun addWeekDays() {
        mArrayDays.add(mContext.resources.getString(R.string.sun))
        mArrayDays.add(mContext.resources.getString(R.string.mon))
        mArrayDays.add(mContext.resources.getString(R.string.tue))
        mArrayDays.add(mContext.resources.getString(R.string.wed))
        mArrayDays.add(mContext.resources.getString(R.string.thu))
        mArrayDays.add(mContext.resources.getString(R.string.fri))
        mArrayDays.add(mContext.resources.getString(R.string.sat))
    }
}