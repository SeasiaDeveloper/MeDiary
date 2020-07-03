package com.mediary.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object Utilities {
    private lateinit var formattedDate: String
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 121

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun checkPermission(context: Context, permission : String, requestCode : Int): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        permission
                    )
                ) {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(android.R.string.yes
                    ) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(permission),
                            requestCode
                        )
                    }
                    val alert = alertBuilder.create()
                    alert.show()

                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(permission),
                        requestCode
                    )
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun rotateMyImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }


    fun getBitmapFromUri(context: Context,uri: Uri) : Bitmap? {
        var bm: Bitmap? = null

        if (uri != null) {
            try {
                bm =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bm
    }
    /**
     * Show toast message
     */
    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * This method used to hide keyboard
     */
    fun hideKeyboard(context: Activity) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
         //   Log.d("Exception", e?.message.)
        }
    }

    fun setCurrentDate(): Date {
        return Calendar.getInstance().time
    }
    fun getFormattedDate(millis : Long): Date {
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date(millis))
        return dateFormat.parse(dateString)
    }
    fun dbDateFormat(date: String): String {
        val oldDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val newDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val convertedDate: Date?
        try {
            convertedDate = oldDateFormat.parse(date)
            formattedDate = newDateFormat.format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formattedDate
    }

    fun dbTimeFormat(time: String): String {
        val oldDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val convertedDate: Date?
        try {
            convertedDate = oldDateFormat.parse(time)
            formattedDate = newDateFormat.format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formattedDate
    }

}
