package com.mediary.activities.applock

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mediary.R
import com.kevalpatel.passcodeview.authenticator.PasscodeViewPinAuthenticator
import com.kevalpatel.passcodeview.indicators.CircleIndicator
import com.kevalpatel.passcodeview.keys.KeyNamesBuilder
import kotlinx.android.synthetic.main.activity_passcode_applock_activity.*
import com.kevalpatel.passcodeview.keys.RoundKey




class PassCodeAppLockActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_passcode_applock_activity)
        val correctPin = intArrayOf(1, 2, 3, 4)
        pin_view.pinAuthenticator = PasscodeViewPinAuthenticator(correctPin)
        pin_view.setKey(RoundKey.Builder(pin_view)
            .setKeyPadding(R.dimen.lib_key_padding)
            .setKeyStrokeColorResource(R.color.colorAccent)
            .setKeyStrokeWidth(R.dimen.lib_key_stroke_width)
            .setKeyTextColorResource(R.color.colorAccent)
            .setKeyTextSize(R.dimen.lib_key_text_size));
        pin_view.setKeyNames( KeyNamesBuilder()
            .setKeyOne(this, R.string.key_1)
            .setKeyTwo(this, R.string.key_2)
            .setKeyThree(this, R.string.key_3)
            .setKeyFour(this, R.string.key_4)
            .setKeyFive(this, R.string.key_5)
            .setKeySix(this, R.string.key_6)
            .setKeySeven(this, R.string.key_7)
            .setKeyEight(this, R.string.key_8)
            .setKeyNine(this, R.string.key_9)
            .setKeyZero(this, R.string.key_0))

        pin_view.setIndicator( CircleIndicator.Builder(pin_view)
            .setIndicatorRadius(R.dimen.lib_indicator_radius)
            .setIndicatorFilledColorResource(R.color.colorAccent)
            .setIndicatorStrokeColorResource(R.color.colorAccent)
            .setIndicatorStrokeWidth(R.dimen.lib_indicator_stroke_width));
    }

}