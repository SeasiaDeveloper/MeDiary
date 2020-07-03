package com.mediary.fragments

import android.app.Activity.RESULT_OK
import android.app.KeyguardManager
import android.content.Context.KEYGUARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity
import com.mediary.adapters.ViewPagerAdapter
import com.mediary.passcode.PasscodeViewActivity
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.tutorial_main_fragment.*


class TutorialMainFragment : Fragment(), View.OnClickListener {
    private lateinit var km: KeyguardManager
    private val CODE_AUTHENTICATION_VERIFICATION = 241
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(fragmentManager!!)
        adapter.setFragments()
        imageViewPager.adapter = adapter
        spring_dots_indicator.setViewPager(imageViewPager)
        onCircleButtonClick()
        imageViewPager.offscreenPageLimit = 2
        km = activity!!.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tutorial_main_fragment, container, false)
    }

    private fun onCircleButtonClick() {
        btnStart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStart -> {
                PreferenceHandler.writeBoolean(context!!, PreferenceHandler.IS_INSTALLED, true)
                if (km.isKeyguardSecure())
                {
                    val i = km.createConfirmDeviceCredentialIntent("Authentication required", "password")
                    startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION)
                }
                else {
                    startActivity(Intent(context,PasscodeViewActivity::class.java))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
            startActivity(Intent(context,DashboardActivity::class.java))
            activity!!.finish()
        } else {
            Toast.makeText(
                activity,
                "Failure: Unable to verify user's identity",
                Toast.LENGTH_SHORT
            ).show();
        }
    }

}