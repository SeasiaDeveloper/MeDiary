package com.mediary.adapters

import android.content.Context
import android.text.Html
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mediary.R
import com.mediary.fragments.TutorialFragmentOne
import com.squareup.picasso.Picasso

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private lateinit var frag1:TutorialFragmentOne
    private lateinit var frag2:TutorialFragmentOne
    private lateinit var frag3:TutorialFragmentOne


    fun setFragments() {
        frag1 =TutorialFragmentOne()
        frag2 = TutorialFragmentOne()
        frag3 = TutorialFragmentOne()
        frag1.changeText(  HtmlCompat.fromHtml("Welcome to ME DIARY! <br> </br><br> </br><br><font color=\"#949494\">In ME DIARY, daily entries are created using Grids.Each grid comes with a prompt title, which can guide you to note down your memories.</font></br>", HtmlCompat.FROM_HTML_MODE_LEGACY))
        frag2.changeText(HtmlCompat.fromHtml("Customize Grids <br> </br><br> </br><br><font color=\"#949494\">Design your own diary template for daily use, or customize different grids for each entry.</font></br>",HtmlCompat.FROM_HTML_MODE_LEGACY))
        frag3.changeText(  HtmlCompat.fromHtml("Reading Mode <br> </br><br> </br><br><font color=\"#949494\">Read your diary or share it in the reading mode. Your diary looks gorgeous like never before.</font></br>",HtmlCompat.FROM_HTML_MODE_LEGACY))
        frag1.changeImage(R.drawable.screen1)
        frag2.changeImage(R.drawable.screen4)
        frag3.changeImage(R.drawable.screen3)
    }
    override fun getItem(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment =frag1
            1 -> fragment = frag2
            2 -> fragment = frag3
        }
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}