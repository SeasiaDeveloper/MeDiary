package com.mediary.activities

import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediary.MeDiaryApplication
import com.mediary.R
import com.mediary.adapters.StaticColorSetAdapter
import com.mediary.adapters.StaticColorSetAdapter.OnStaticColorSelectedListener
import com.mediary.base.BaseActivity
import com.mediary.utils.Constants
import com.mediary.utils.MyColor
import com.mediary.utils.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_color_chooser.*
import kotlinx.android.synthetic.main.fragment_color_chooser.toolbar
import top.defaults.colorpicker.*
import java.util.*

class ColorChooseActivity
    : BaseActivity(), ColorObserver,
    CompoundButton.OnCheckedChangeListener, OnStaticColorSelectedListener, Toolbar.OnMenuItemClickListener {
    private var observableOnDuty: ColorObservable? = null
    private  var alphaSliderView: AlphaSliderView?=null
    private var switchOnOff: Switch? = null
    private val open = false
    private val mListener: OnColorChooserFragInteractionListener? = null
    private var mProgress = 30
    private val myPopupWindow: PopupWindow? = null
    private var colorWheelView: ColorWheelView? = null
    private var picker: ColorPickerView? = null
    private val sliderMargin = 0
    private val sliderHeight = 0
    var observers: List<ColorObserver> = ArrayList()


    override fun getLayout(): Int {
        return R.layout.fragment_color_chooser
    }

    override fun setupUI() {
        switchOnOff = findViewById(R.id.switchLights)
        switchOnOff!!.setOnCheckedChangeListener(this)
        colorWheelView = ColorWheelView(this)
        toolbar.title = getString(R.string.choose_color)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.inflateMenu(R.menu.add_done_button)
        toolbar.setOnMenuItemClickListener(this)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        picker = findViewById<View>(R.id.colorPicker) as ColorPickerView
        picker!!.setBrightnessSliderView(brightnessSliderView)
        picker!!.subscribe(this)
        val ivMenu = findViewById<ImageView>(R.id.ivMenu)
        ivMenu.setOnClickListener { v ->
            val location = IntArray(2)
            v.getLocationOnScreen(location)
            myPopupWindow!!.showAsDropDown(v, -130, -125)
        }
      /*  val seekBarBrightness = findViewById<SeekBar>(R.id.seekBarMusic)
        seekBarBrightness.setOnSeekBarChangeListener(this)*/
        if (MeDiaryApplication.savedColor != null) {
           // seekBarBrightness.progress = MeDiaryApplication.savedColor.getProgress()
            mProgress = MeDiaryApplication.savedColor.getProgress()
          //  seekBarBrightness.progress = mProgress
            picker!!.setInitialColor(MeDiaryApplication.savedColor.getColor())
            mListener?.onColorSelected(
                MyColor(
                    MeDiaryApplication.savedColor.getColor(),
                    0.toByte(),
                    mProgress
                )
            )
        } else {
            picker!!.setInitialColor(Color.parseColor("#FFFFFF"))
            MeDiaryApplication.savedColor.setColor(Color.parseColor("#FFFFFF"))
            MeDiaryApplication.savedColor.setProgress(30)
        }
       //setEnabledBrightness(true)


        picker!!.setEnabledBrightness(true)
        setStaticColorsAdapter()
    }

    override fun handleKeyboard(): View {
        return fragment_color_chooser
    }

    public override fun onResume() {
        super.onResume()
        if (mListener != null) {
            if (MeDiaryApplication.savedColor == null) return
            if (switchOnOff!!.isChecked) {
                mListener.onColorSelected(
                    MyColor(
                        MeDiaryApplication.savedColor.getColor(),
                        0.toByte(),
                        MeDiaryApplication.savedColor.getProgress()
                    )
                )
            }
        }
    }

    private fun setStaticColorsAdapter() {
        val rvColorSet = findViewById<RecyclerView>(R.id.rvColorSet)
        rvColorSet.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        rvColorSet.itemAnimator = DefaultItemAnimator()
        val adapter = StaticColorSetAdapter(Objects.requireNonNull(this), this)
        rvColorSet.adapter = adapter
    }

    override fun onColor(color: Int, fromUser: Boolean) {
        saveColor(color)
        tvSelectedColor!!.setBackgroundColor(color)
        PreferenceHandler.writeInteger(this, PreferenceHandler.COLOR_SELECTION, color)
        if (switchOnOff!!.isChecked) onColorSelected(color)
    }

    private fun saveColor(color: Int) {
        MeDiaryApplication.savedColor.setColor(color)
        MeDiaryApplication.savedColor.setWarmWhite(0.toByte())
        MeDiaryApplication.savedColor.setProgress(mProgress)
    }

    override fun onCheckedChanged(
        buttonView: CompoundButton,
        isChecked: Boolean
    ) {
        MeDiaryApplication.isLightsOn = isChecked
        onSwitchPressed(isChecked)
    }

    fun onSwitchPressed(isOpen: Boolean) {
        mListener?.onSwitchPressed(isOpen, switchOnOff)
    }

    fun onColorSelected(color: Int) {
        if (mListener != null) {
            saveColor(color)
            mListener.onColorSelected(MyColor(color, 0.toByte(), mProgress))
        }
    }

    override fun onStaticColorSelected(selectedColor: Int) {
        tvSelectedColor!!.setBackgroundColor(selectedColor)
        saveColor(selectedColor)
         mListener?.onColorSelected(MyColor(selectedColor, 0.toByte(), mProgress))
        PreferenceHandler.writeInteger(this, PreferenceHandler.COLOR_SELECTION, selectedColor)

    }
/*
    override fun onProgressChanged(
        seekBar: SeekBar,
        progress: Int,
        fromUser: Boolean
    ) {
        picker!!.setEnabledBrightness(true)
    }*/

   /* override fun onStopTrackingTouch(seekBar: SeekBar) {
        val progress = seekBar.progress
        mProgress = if (progress > 0) progress else 1
        MeDiaryApplication.savedColor.setProgress(mProgress)
        if (mListener != null) {
            mListener.onColorSelected(
                MyColor(
                    MeDiaryApplication.savedColor.getColor(),
                    0.toByte(),
                    mProgress
                )
            )
            if (MeDiaryApplication.savedColor == null) return
            if (switchOnOff!!.isChecked) {
                mListener.onColorSelected(
                    MyColor(
                        MeDiaryApplication.savedColor.getColor(),
                        0.toByte(),
                        mProgress
                    )
                )
            }
        }
    }*/

    interface OnColorChooserFragInteractionListener {
        fun onSwitchPressed(
            isOpen: Boolean,
            switchOnOff: Switch?
        )

        fun onColorSelected(color: MyColor?)
        fun onPopupWindowPressed(isAudio: Boolean)
        fun onPopupWindowStyleBtnPressed()
    }

    fun setEnabledBrightness(enable: Boolean) {
        if (enable) {
            if (brightnessSliderView == null) {
                val params =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        sliderHeight
                    )
                params.topMargin = sliderMargin
        //   addView(brightnessSliderView, 1, params)
            }
            brightnessSliderView.bind(colorWheelView)
            updateObservableOnDuty()
        } else {
            if (brightnessSliderView != null) {
                brightnessSliderView.unbind()
               // removeView(brightnessSliderView)
            }
            updateObservableOnDuty()
        }
        if (alphaSliderView != null) {
            setEnabledAlpha(true)
        }
    }

  fun setEnabledAlpha(enable: Boolean) {
        if (enable) {
            if (alphaSliderView == null) {
                alphaSliderView = AlphaSliderView(this)
                val params =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        sliderHeight
                    )
                params.topMargin = sliderMargin
               // addView(alphaSliderView, params)
            }
            var bindTo: ColorObservable? = brightnessSliderView
            if (bindTo == null) {
                bindTo = colorWheelView
            }
            alphaSliderView?.bind(bindTo)
            updateObservableOnDuty()
        } else {
            if (alphaSliderView != null) {
                alphaSliderView?.unbind()
              //  removeView(alphaSliderView)
            }
            updateObservableOnDuty()
        }
    }

    private fun updateObservableOnDuty() {
        if (observableOnDuty != null) {
            for (observer in observers) {
                observableOnDuty?.unsubscribe(observer)
            }
        }
        if (brightnessSliderView == null && alphaSliderView == null) {
            observableOnDuty = colorWheelView!!
        } else {
            if (alphaSliderView != null) {
                observableOnDuty = alphaSliderView
            } else {
                observableOnDuty = brightnessSliderView
            }
        }
        if (observers != null) {
            for (observer in observers) {
                observableOnDuty?.subscribe(observer)
                observer.onColor(observableOnDuty!!.getColor(), false)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.btnDone) {
            val colorSelected =
                PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
            Constants.isColorButtonClicked = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = this.window
                val statusBarColor = colorSelected
                if (statusBarColor == Color.BLACK && window.navigationBarColor === Color.BLACK) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                }
                window.statusBarColor = statusBarColor
            }
            finish()
        }
        return false
    }
}