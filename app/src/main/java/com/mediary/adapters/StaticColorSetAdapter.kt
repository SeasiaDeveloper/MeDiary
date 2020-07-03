package com.mediary.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import java.util.*

class StaticColorSetAdapter(
    context: Context,
    private val onStaticColorSelectedListener: OnStaticColorSelectedListener
) : RecyclerView.Adapter<StaticColorSetAdapter.MyViewHolder>() {
    private val listColors = ArrayList<StaticColor>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color_set, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val color = listColors[position]
        holder.tvColor.setBackgroundColor(color.color)
        if (color.isSelected) holder.ivTick.visibility =
            View.VISIBLE else holder.ivTick.visibility = View.INVISIBLE
        holder.tvColor.setOnClickListener {
            onStaticColorSelectedListener.onStaticColorSelected(color.color)
            for (staticColor in listColors) {
                if (color.color == staticColor.color) {
                    staticColor.isSelected = true
                } else {
                    staticColor.isSelected = false
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return listColors.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvColor: TextView
        var ivTick: ImageView

        init {
            tvColor = itemView.findViewById(R.id.tvColor)
            ivTick = itemView.findViewById(R.id.ivHook)
        }
    }

    interface OnStaticColorSelectedListener {
        fun onStaticColorSelected(selectedColor: Int)
    }

    private inner class StaticColor(var color: Int, var isSelected: Boolean)

    init {
        val colorSet = ArrayList(
            Arrays.asList(
                *context.resources.getStringArray(R.array.color_set)
            )
        )
        for (color in colorSet) {
            listColors.add(StaticColor(Color.parseColor(color), false))
        }
    }
}