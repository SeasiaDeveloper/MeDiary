package com.mediary.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediary.R
import kotlinx.android.synthetic.main.fragment_three.*

class TutorialFragmentThree : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text =
            Html.fromHtml("Reading Mode <br> </br><br> </br><br><font color=\"#949494\">Read your diary or share it in the reading mode. Your diary looks gorgeous like never before.</font></br>")
        txtReadingMode.text = text
    }
}