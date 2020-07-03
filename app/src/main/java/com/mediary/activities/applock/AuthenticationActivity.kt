package com.mediary.activities.applock

import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.mediary.R
import com.mediary.activities.dashboard.DashboardActivity
import com.mediary.base.BaseActivity
import com.mediary.listeners.AuthenticationPresenter
import com.mediary.models.AuthenticationModel
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import com.multidots.fingerprintauth.FingerPrintAuthCallback
import com.multidots.fingerprintauth.FingerPrintAuthHelper
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : BaseActivity(), AuthenticationPresenter, FingerPrintAuthCallback {

    private lateinit var mAuthenticationModel: AuthenticationModel
    private var mFingerPrintAuthHelper: FingerPrintAuthHelper? = null
    private var alertDialog: AlertDialog? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var whichAuthMethod = 0

    override fun getLayout(): Int {
        return R.layout.activity_authentication
    }

    override fun setupUI() {
        val window = this.window
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            if (Constants.isColorButtonClicked) {
                val colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
                window.statusBarColor = ContextCompat.getColor(this, colorSelected)
            } else {
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
            }
        }
        toolbar.setTitle(R.string.authentication)
        mAuthenticationModel = AuthenticationModel(this, this, this@AuthenticationActivity)
        whichAuthMethod = 0

    }

    override fun handleKeyboard(): View {
        return authenticationParent
    }

    private fun openBiometricAuthenticationDialog() {
        dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.layout_fingerprint_authentication, null)
        dialogBuilder!!.setView(dialogView)
        alertDialog = dialogBuilder!!.create()
        alertDialog!!.show()
        alertDialog!!.setOnDismissListener { _ -> onBackPressed() }
    }

    override fun isCancel() {
        val intent = Intent(this@AuthenticationActivity, PassCodeAppLockActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onNoFingerPrintHardwareFound() {
        com.mediary.utils.Utilities.showMessage(
            this,
            getString(R.string.device_does_not_have_fingerprint_sensor_available)
        )
        mAuthenticationModel.openNormalDialog()
        whichAuthMethod = 1
    }

    override fun onAuthFailed(errorCode: Int, errorMessage: String?) {
        Log.e("Err", "Error $errorMessage")
        if (errorCode != 566)
            runOnUiThread {
                try {
                    mAuthenticationModel.openPasscodeDialog()
                    whichAuthMethod = 1

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    override fun onNoFingerPrintRegistered() {
        com.mediary.utils.Utilities.showMessage(
            this,
            "User did not register any fingerprint in the device"
        )
        mAuthenticationModel.openPasscodeDialog()
        whichAuthMethod = 1
    }

    override fun onBelowMarshmallow() {
        mAuthenticationModel.openPasscodeDialog()
        whichAuthMethod = 1
    }

    override fun onAuthSuccess(cryptoObject: FingerprintManager.CryptoObject?) {
        alertDialog!!.cancel()
        startActivity(Intent(this@AuthenticationActivity, DashboardActivity::class.java))
        finish()
    }

    override fun onPause() {
        super.onPause()
        if (alertDialog != null) {
            alertDialog!!.cancel()
        }
    }

    override fun onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            openBiometricAuthenticationDialog()
            mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this)
            mFingerPrintAuthHelper!!.startAuth()
        } else {
            whichAuthMethod = 1
            mAuthenticationModel.openNormalDialog()
        }
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (whichAuthMethod == 0) {
            whichAuthMethod = 1
            mAuthenticationModel.openPasscodeDialog()
        } else {
            finish()
        }
    }
}