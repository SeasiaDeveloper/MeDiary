package com.mediary.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.activities.moodweather.view.MoodWeatherView
import com.mediary.models.WeatherOptions
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_mood_weather_log.*
import kotlinx.android.synthetic.main.mood_weather_grid_view_items.view.*

class MoodWeatherAdapter(
    private var context: Context,
    private var weatherList: ArrayList<WeatherOptions>,
    private var moodWeatherView: MoodWeatherView,
    private var selectedWeatherPosition: Int,
    private var isWeatherExist: Boolean
) : RecyclerView.Adapter<MoodWeatherAdapter.PromptViewHolder>() {
    private var isFirst = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mood_weather_grid_view_items, parent, false)
        return PromptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: PromptViewHolder, position: Int) {
        val weatherList = weatherList[position]
        if (weatherList != null) {
            if (isWeatherExist) {
                if (position == selectedWeatherPosition) {
                    val colorSelected =
                        PreferenceHandler.readInteger(context, PreferenceHandler.COLOR_SELECTION, 0)
                    if (colorSelected != 0) {
                        holder.imageView.setColorFilter(colorSelected, PorterDuff.Mode.SRC_IN)
                    } else {
                        holder.imageView.setColorFilter(
                            ContextCompat.getColor(context, R.color.colorPrimary),
                            android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    }

                } else {
                    holder.imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            } else {
                holder.imageView.setColorFilter(
                    ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                isWeatherExist = true
            }
            holder.imageView.setImageResource(weatherList.images)
            holder.imageView.setOnClickListener {
                if (selectedWeatherPosition == position) {
                    selectedWeatherPosition = -1
                    holder.imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    moodWeatherView.showWeatherOptions(selectedWeatherPosition)
                } else {
                    selectedWeatherPosition = position
                    val colorSelected =
                        PreferenceHandler.readInteger(context, PreferenceHandler.COLOR_SELECTION, 0)
                    if (colorSelected != 0) {
                        holder.imageView.setColorFilter(colorSelected, PorterDuff.Mode.SRC_IN)
                    } else {
                        holder.imageView.setColorFilter(
                            ContextCompat.getColor(context, R.color.colorPrimary),
                            android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    }
                    moodWeatherView.showWeatherOptions(position)
                }
                notifyDataSetChanged()
            }
        }
    }

    class PromptViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.grid_item_image

    }

}