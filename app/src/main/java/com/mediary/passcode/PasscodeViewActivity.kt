package com.mediary.passcode

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kevalpatel.passcodeview.PinView
import com.kevalpatel.passcodeview.authenticator.PasscodeViewPinAuthenticator
import com.kevalpatel.passcodeview.indicators.CircleIndicator
import com.kevalpatel.passcodeview.interfaces.AuthenticationListener
import com.kevalpatel.passcodeview.keys.KeyNamesBuilder
import com.kevalpatel.passcodeview.keys.RoundKey
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity
import com.mediary.base.BaseActivity
import kotlinx.android.synthetic.main.activity_passcodeview.*


class PasscodeViewActivity : BaseActivity() {
    private val ARG_CURRENT_PIN = "current_pin"

    override fun getLayout(): Int {
        return R.layout.activity_passcodeview
    }

    override fun setupUI() {
        val correctPin = intArrayOf(1, 2, 3, 4)
        pin_view.pinAuthenticator = PasscodeViewPinAuthenticator(correctPin)
        pin_view.setKey(
            RoundKey.Builder(pin_view)
                .setKeyPadding(R.dimen.key_padding)
                .setKeyStrokeColorResource(R.color.black)
                .setKeyStrokeWidth(R.dimen.key_stroke_width)
                .setKeyTextColorResource(R.color.black)
                .setKeyTextSize(R.dimen.key_text_size)
        )
        pin_view.setIndicator(
            CircleIndicator.Builder(pin_view)
                .setIndicatorRadius(R.dimen.indicator_radius)
                .setIndicatorFilledColorResource(R.color.black)
                .setIndicatorStrokeColorResource(R.color.black)
                .setIndicatorStrokeWidth(R.dimen.indicator_stroke_width)
        )
        pin_view.pinLength = 4
        pin_view.setKeyNames(
            KeyNamesBuilder()
                .setKeyOne(this, R.string.key_1)
                .setKeyTwo(this, R.string.key_2)
                .setKeyThree(this, R.string.key_3)
                .setKeyFour(this, R.string.key_4)
                .setKeyFive(this, R.string.key_5)
                .setKeySix(this, R.string.key_6)
                .setKeySeven(this, R.string.key_7)
                .setKeyEight(this, R.string.key_8)
                .setKeyNine(this, R.string.key_9)
                .setKeyZero(this, R.string.key_0)
        )
        pin_view.title = "Enter the PIN"
        pin_view.setIsFingerPrintEnable(false)
        pin_view.setAuthenticationListener(object : AuthenticationListener {
            override fun onAuthenticationSuccessful() {
                startActivity(Intent(this@PasscodeViewActivity, DashboardActivity::class.java))
                finish()
            }

            override fun onAuthenticationFailed() {
                //Calls whenever authentication is failed or user is unauthorized.
                //Do something if you want to handle unauthorized user.
            }
        })
    }

    override fun handleKeyboard(): View {
        return passcodeParent
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray(ARG_CURRENT_PIN, pin_view.currentTypedPin)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pin_view.currentTypedPin = savedInstanceState.getIntArray(ARG_CURRENT_PIN)

    }
}