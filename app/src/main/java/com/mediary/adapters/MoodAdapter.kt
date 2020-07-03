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
import com.mediary.models.MoodOptions
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.mood_weather_grid_view_items.view.*

class MoodAdapter(
    private var context: Context,
    private var emojiList: ArrayList<MoodOptions>,
    private var moodWeatherView: MoodWeatherView,
    private var selectedPosition: Int,
    private var isExist: Boolean
) : RecyclerView.Adapter<MoodAdapter.PromptViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mood_weather_grid_view_items, parent, false)
        return PromptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: PromptViewHolder, position: Int) {
        val weatherList = emojiList[position]
        if (weatherList != null) {
            if (isExist) {
                if (position == selectedPosition) {
                    val colorSelected = PreferenceHandler.readInteger(context, PreferenceHandler.COLOR_SELECTION, 0)
                    if (colorSelected != 0){
                        holder.imageView.setColorFilter(colorSelected, PorterDuff.Mode.SRC_IN)
                    }else {
                        holder.imageView.setColorFilter(
                            ContextCompat.getColor(context, R.color.colorPrimary),
                            PorterDuff.Mode.SRC_IN
                        )
                    }
                }else {
                    holder.imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN)
                }
           } else {
                holder.imageView.setColorFilter(
                    ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN)
                isExist=true
            }

            holder.imageView.setImageResource(weatherList.images)
            holder.imageView.setOnClickListener { v ->
                if(selectedPosition == position) {
                    selectedPosition = -1
                    holder.imageView.setColorFilter(
                        ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN)
                    moodWeatherView.showMoodOptions(selectedPosition)
                }else {
                    selectedPosition = position
                    val colorSelected = PreferenceHandler.readInteger(context, PreferenceHandler.COLOR_SELECTION, 0)
                    if (colorSelected != 0){
                        holder.imageView.setColorFilter(colorSelected, PorterDuff.Mode.SRC_IN)
                    }else {
                        holder.imageView.setColorFilter(
                            ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN
                        )                    }
                    moodWeatherView.showMoodOptions(position)
                }
                notifyDataSetChanged()
            }
        }
    }

    class PromptViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.grid_item_image
    }

}

