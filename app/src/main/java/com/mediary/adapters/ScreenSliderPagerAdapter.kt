package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.mediary.R
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.utils.Utilities
import kotlinx.android.synthetic.main.adapter_screen_slider.view.*


class ScreenSliderPagerAdapter(
    private val c: Context,
    private var list: ArrayList<MeDairyImagesEntity>?
) : PagerAdapter() {

    override fun isViewFromObject(v: View, `object`: Any): Boolean {
        // Return the current view
        return v === `object` as View
    }

    override fun getCount(): Int {
        // Count the items and return it
        return list!!.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_screen_slider, parent, false)
        val path = list?.get(position)
        if (path != null) {
            val bm: Bitmap = Utilities.getBitmapFromUri(c, Uri.parse(path.imagesPath))!!
            view.sliderImageView.setImageBitmap(bm)
        }
        parent.addView(view)
        return view
    }

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) {
        parent.removeView(`object` as View)
    }

    fun setList(mListOfGalleryImages: java.util.ArrayList<MeDairyImagesEntity>) {
        list = mListOfGalleryImages
    }
}