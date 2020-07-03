package com.mediary.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.myapplication.ScreenSliderPagerAdapter
import com.mediary.R
import com.mediary.base.BaseActivity
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.activity_image_slider.*

class ImageSliderActivity : BaseActivity(), View.OnClickListener {
    private var mListOfGalleryImages: ArrayList<MeDairyImagesEntity>? = null
    private var imagePosition: Int? = null
    private lateinit var adapter: ScreenSliderPagerAdapter
    private lateinit var dateString: String

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.seeALL -> {
                val intent = Intent(this, ImagesGridActivity::class.java)
                val b = Bundle()
                b.putSerializable(Constants.LIST_OF_GALLERY_IMAGES, mListOfGalleryImages)
                b.putString(Constants.DATESTRING, dateString)
                intent.putExtras(b)
                startActivity(intent)
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
            }
            R.id.delete -> {
                val position = viewPager.currentItem
                database?.imagesDao()
                    ?.deleteParticularImage(mListOfGalleryImages!![position].imagesPath)
                mListOfGalleryImages =
                    database?.imagesDao()
                        ?.getImagesFromDB(dateString) as java.util.ArrayList<MeDairyImagesEntity>
                if (mListOfGalleryImages!!.size > 0) {
                    adapter.setList(mListOfGalleryImages!!)
                    adapter.notifyDataSetChanged()
                } else {
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.fade_out)
                }

            }
            R.id.btnCross -> {
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.fade_out)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    override fun getLayout(): Int {
        return R.layout.activity_image_slider
    }

    override fun setupUI() {

        val bundle: Bundle? = intent.extras
        mListOfGalleryImages =
            bundle?.getSerializable(Constants.LIST_OF_GALLERY_IMAGES) as ArrayList<MeDairyImagesEntity>
        imagePosition = bundle.getInt(Constants.IMAGE_POSITION)
        dateString = bundle.getString(Constants.DATESTRING).toString()
        adapter = ScreenSliderPagerAdapter(this, mListOfGalleryImages)
        viewPager.adapter = adapter
        viewPager.currentItem = imagePosition!!
        seeALL.setOnClickListener(this)
        delete.setOnClickListener(this)
        btnCross.setOnClickListener(this)
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            seeALL.setBackgroundColor(colorSelected)
            delete.setBackgroundColor(colorSelected)
            btnCross.setBackgroundColor(colorSelected)
        }
    }

    override fun handleKeyboard(): View {
        return rootLayout
    }

    override fun onResume() {
        super.onResume()
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            seeALL.setBackgroundColor(colorSelected)
            delete.setBackgroundColor(colorSelected)
            btnCross.setBackgroundColor(colorSelected)
        }

    }
}
