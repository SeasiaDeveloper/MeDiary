package com.mediary.models

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.mediary.R
import com.mediary.activities.applock.AuthenticationActivity
import com.mediary.listeners.AuthenticationPresenter


class AuthenticationModel(
    private val mContext: Context,
    private val mAuthenticationInterface: AuthenticationPresenter,
    private val mActivity: AuthenticationActivity
) {

    fun openPasscodeDialog() {
        val alertDialog1 = AlertDialog.Builder(mActivity) //Read Update
        alertDialog1.setTitle("Me Diary")
        alertDialog1.setMessage(" Touch ID for Me Diary , To access the secure data")
        alertDialog1.setNegativeButton("Cancel") { dialog, which -> mAuthenticationInterface.isCancel() }
        try {
            alertDialog1.show()

        } catch (e: Exception) {

        }

    }

    fun openNormalDialog() {
        val alertDialog = AlertDialog.Builder(mActivity) //Read Update
        alertDialog.setTitle("Me Diary")
        alertDialog.setMessage(mContext.getString(R.string.authentication_dialog_text))
        alertDialog.setNegativeButton("Set Passcode") { dialog, which -> mAuthenticationInterface.isCancel() }
        alertDialog.show()
    }


}