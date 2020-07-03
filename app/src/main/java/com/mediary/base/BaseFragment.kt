package com.mediary.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.mediary.database.AppDatabase
import com.mediary.utils.PreferenceHandler


abstract class BaseFragment : Fragment() {
    lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(context!!)!!
        setupUI(view)
        changeAppStatusBar()
    }

    private fun changeAppStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity!!.window
            val statusBarColor =
                PreferenceHandler.readInteger(activity!!, PreferenceHandler.COLOR_SELECTION, 0)
            if (statusBarColor == Color.BLACK && window.navigationBarColor === Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if(statusBarColor !=0)
                window.statusBarColor = statusBarColor
        }
    }
    protected abstract fun getLayout(): Int
    protected abstract fun setupUI(view: View)

}