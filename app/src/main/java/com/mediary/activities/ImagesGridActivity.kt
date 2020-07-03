package com.mediary.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediary.R
import com.mediary.adapters.ImageGridKotlinAdapter
import com.mediary.base.BaseActivity
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.utils.Constants
import kotlinx.android.synthetic.main.activity_grid_images.*


class ImagesGridActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.btnClose -> {
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.top_bottom)
            }
        }
    }

    private var mListOfGalleryImages: ArrayList<MeDairyImagesEntity>? = null
    private lateinit var dateString:String

    override fun getLayout(): Int {
        return R.layout.activity_grid_images
    }

    override fun setupUI() {
        val bundle: Bundle? = intent.extras
        mListOfGalleryImages =
            bundle?.getSerializable(Constants.LIST_OF_GALLERY_IMAGES) as ArrayList<MeDairyImagesEntity>
        dateString= bundle.getString(Constants.DATESTRING).toString()
        setAdapter()
        btnClose.setOnClickListener(this)
    }

    override fun handleKeyboard(): View {
        return parentView
    }

    private fun setAdapter() {
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        val recyclerAdapter = ImageGridKotlinAdapter(this, mListOfGalleryImages!!,dateString)
        recyclerView.adapter = recyclerAdapter
    }

}
