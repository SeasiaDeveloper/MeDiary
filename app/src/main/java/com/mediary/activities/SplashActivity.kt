package com.mediary.activities

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity
import com.mediary.base.BaseActivity
import com.mediary.passcode.PasscodeViewActivity
import com.mediary.receiver.CheckAppStatusService
import com.mediary.receiver.CheckRecentRun
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    private lateinit var km: KeyguardManager
    private val CODE_AUTHENTICATION_VERIFICATION = 241

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun setupUI() {
        km = this.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        // startService(Intent(this, CheckAppStatusService::class.java))
        Handler().postDelayed({
            run {
                if (PreferenceHandler.readBoolean(
                        this@SplashActivity,
                        PreferenceHandler.IS_INSTALLED,
                        false
                    )
                ) {
                    if (km.isKeyguardSecure()) {
                        val i = km.createConfirmDeviceCredentialIntent(
                            "Authentication required",
                            "password"
                        )
                        startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION)
                    }
                    /*   mIntent= Intent(this@SplashActivity, DashboardActivity::class.java)
                       finish()*/
                } else {
                    var mIntent = Intent(this@SplashActivity, TutorialsActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }
            }
        }, 2000)

    }

    override fun handleKeyboard(): View {
        return splashParent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
            var mIntent = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(mIntent)
            finish()
        } else {
            Toast.makeText(
                this,
                "Failure: Unable to verify user's identity",
                Toast.LENGTH_SHORT
            ).show();
        }
    }

}