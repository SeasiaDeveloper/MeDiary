package com.mediary.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediary.R
import kotlinx.android.synthetic.main.fragment_two.*

class TutorialFragmentTwo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text =
            Html.fromHtml("Customize Grids <br> </br><br> </br><br><font color=\"#949494\">Design your own diary template for daily use, or customize different grids for each entry.</font></br>")
        txtGrid.text = text
        /*  ObjectAnimator.ofFloat(mobile, "translationX", 100f).apply {

              start()
          }*/

    }
}