package com.mediary.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.activities.ImageSliderActivity
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.utils.Constants
import com.mediary.utils.Utilities
import kotlinx.android.synthetic.main.adapter_item_image_grid.view.*

class ImageGridKotlinAdapter(
    private val c: Context,
    private val images: ArrayList<MeDairyImagesEntity>,
    private var dateString: String
) :
    RecyclerView.Adapter<ImageGridKotlinAdapter.ColorViewHolder>() {


    override fun getItemCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_image_grid, parent, false))
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val path = images[position]
            holder.iv.setImageBitmap(Utilities.getBitmapFromUri(c, Uri.parse(path.imagesPath)))
        holder.iv.setOnClickListener {
            val intent = Intent(c, ImageSliderActivity::class.java)
            val b = Bundle()
            b.putInt(Constants.IMAGE_POSITION, position)
            b.putSerializable(Constants.LIST_OF_GALLERY_IMAGES, images)
            b.putString(Constants.DATESTRING,dateString)
            intent.putExtras(b)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            c.startActivity(intent)
            (c as Activity).finish()
        }
    }

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv = view.iv as ImageView
    }
}