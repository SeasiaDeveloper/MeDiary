package com.mediary.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mediary.R
import com.mediary.base.BaseActivity
import com.mediary.fragments.TutorialMainFragment
import kotlinx.android.synthetic.main.activity_tutorials.*

class TutorialsActivity : BaseActivity() {
    private lateinit var fragment: Fragment

    override fun getLayout(): Int {
        return R.layout.activity_tutorials
    }

    override fun setupUI() {
        fragment = TutorialMainFragment()
        loadFragment(fragment)
    }

    override fun handleKeyboard(): View {
        return tutorialParentLayout
    }

    /**
     * method to load the fragments on activity
     * ̥*/
    private fun loadFragment(fragment: Fragment) {
        val arguments = Bundle()
        fragment.arguments = arguments
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("")
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}