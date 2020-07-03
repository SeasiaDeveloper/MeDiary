package com.mediary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediary.R
import kotlinx.android.synthetic.main.fragment_one.*
import android.text.Spanned
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_one.img1

class TutorialFragmentOne:Fragment() {
    private lateinit var text:Spanned
    private  var newImage:Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txtWelcome.text = text
        Picasso.get().load(newImage).into( img1)
    }
    fun changeText(newText:Spanned) {
        text = newText
    }
    fun changeImage(image:Int) {
        newImage = image
    }
}