package com.mediary.activities.answerstoquestions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget
import com.google.android.material.tabs.TabLayout
import com.mediary.BuildConfig
import com.mediary.R
import com.mediary.activities.ImageSliderActivity
import com.mediary.activities.answerstoquestions.presenter.QuestionAnswerPresenter
import com.mediary.activities.answerstoquestions.presenter.QuestionAnswerPresenterImpl
import com.mediary.activities.answerstoquestions.view.QuestionAnswerView
import com.mediary.base.BaseActivity
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.database.entity.Questions
import com.mediary.utils.Alert
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import com.mediary.utils.Utilities
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.activity_questionanswers.*
import kotlinx.android.synthetic.main.activity_questionanswers.tvDate
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class QuestionAnswerActivity : BaseActivity(), QuestionAnswerView, TextWatcher,
    SensorEventListener {

    private lateinit var imagesList: ArrayList<MeDairyImagesEntity>
    private var stepCountValue: Float = 0.0f
    private lateinit var sManager: SensorManager
    private var stepSensor: Sensor? = null
    private lateinit var mQuestionAnswerList: List<Questions>
    private var itemPosition: Int = 0
    private var characterLength: CharSequence? = null
    private var bm: Bitmap? = null
    private lateinit var dateString: String
    private var imageUri: Uri? = null
    private var date: String? = null
    private lateinit var tabOne: AppCompatImageView
    private lateinit var tabTWo: AppCompatImageView
    private lateinit var tabThree: AppCompatImageView
    private lateinit var tabFour: AppCompatImageView
    private lateinit var tabFive: AppCompatImageView
    private val REQUEST_CAMERA = 0
    private var IMAGE_MULTIPLE = 1
    private var userChoosenTask: String? = null
    private lateinit var questionAnswerPresenter: QuestionAnswerPresenter
    private var running: Boolean = false
    private lateinit var target1: SimpleTarget
    private var imageSet: Boolean = false

    override fun getLayout(): Int {
        return R.layout.activity_questionanswers
    }

    @SuppressLint("SetTextI18n")
    override fun setupUI() {
        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (database != null) {
            questionAnswerPresenter = QuestionAnswerPresenterImpl(this, database!!)
        }
        val bundle: Bundle? = intent.extras
        mQuestionAnswerList = bundle?.getSerializable(Constants.QUESTIONS_DATA) as List<Questions>
        date = intent.getStringExtra(Constants.DATE)
        itemPosition = intent.getIntExtra(Constants.POSITION, 0)
        tvDate.text = date
        tvDate.typeface = Typeface.DEFAULT_BOLD
        etAnswer.addTextChangedListener(this)
        if (characterLength != null) {
                if (characterLength?.length!! > 0) {
                    tvCharacters.text = "${characterLength?.length.toString().trim()} characters, "
                } else {
                    tvCharacters.text = "${characterLength?.length.toString().trim()} character, "
                }
                tvWords.text = "${wordCount(characterLength.toString().trim())} words"
        }
        setTabLayout()
        setTabIcons()
        imgView.setOnClickListener(View.OnClickListener { view ->
            imageSet = false
            val intent = Intent(this, ImageSliderActivity::class.java)
            val b = Bundle()
            val imagesList = database?.imagesDao()?.setImages(
                mQuestionAnswerList[itemPosition].uid,
                mQuestionAnswerList[itemPosition].joined_date
            ) as ArrayList<MeDairyImagesEntity>
            b.putSerializable(Constants.LIST_OF_GALLERY_IMAGES, imagesList)
            b.putString(Constants.DATESTRING, dateString)
            intent.putExtras(b)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.nothing);
        })
        if (date != null) {
            dateString = Utilities.dbDateFormat(date!!)
        }
        setDataOnView(mQuestionAnswerList[itemPosition])
        if (!PreferenceHandler.readBoolean(
                this@QuestionAnswerActivity,
                PreferenceHandler.IS_QUES_FIRST_TIME,
                false
            ) && PreferenceHandler.readBoolean(
                this@QuestionAnswerActivity,
                PreferenceHandler.IS_INSTALLED,
                true
            )
        ) {
            presentShowcaseSequence()
            PreferenceHandler.writeBoolean(
                this@QuestionAnswerActivity,
                PreferenceHandler.IS_QUES_FIRST_TIME,
                true
            )
        }
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            tvDate.setTextColor(colorSelected)
        }
    }

    private fun presentShowcaseSequence() {
        target1 = SimpleTarget.Builder(this)
            .setTarget(tvQuestion)
            .setHighlightRadius(10f)
            .setDescription("Here you can add images,\n activity data and timestamp.")
            .setStartDelayMillis(500L)
            .setTitle("")
            .build()

        TargetInstructions.with(this@QuestionAnswerActivity)
            .setTargets(arrayListOf(target1))
            .setFadeDuration(200L)
            .setFadeInterpolator(LinearOutSlowInInterpolator())
            .setOverlayColorResId(R.color.colorPrimary) // Background color
            .start()
    }

    private fun setDataOnView(param: Questions) {
        tvQuestion.text = param.question
        etAnswer.setText(param.answers)
        etAnswer.append(param.currentTime + "\n")
        etAnswer.setSelection(etAnswer.text.toString().length)
         imagesList = database?.imagesDao()?.setImages(
            param.uid,
            param.joined_date
        ) as ArrayList<MeDairyImagesEntity>
        if (imagesList.isNotEmpty() && imagesList.size > 0) {
            imgView.visibility = View.VISIBLE
            val path = imagesList[imagesList.size - 1]
            if (path != null) {
                imgView.setImageBitmap(Utilities.getBitmapFromUri(this, Uri.parse(/*path.imagesPath*/
                    imagesList[0].imagesPath)))
            }
        } else {
            imgView.visibility = View.GONE
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not in my use
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            if (event != null) {
                stepCountValue = event.values[0]
            }
        }
    }

    override fun handleKeyboard(): View {
        return questionAnswerParent
    }

    private fun setTabLayout() {
        tabs.addTab(tabs.newTab().setText("calender"))
        tabs.addTab(tabs.newTab().setText("edit"))
        tabs.addTab(tabs.newTab().setText("question"))
        tabs.addTab(tabs.newTab().setText("back"))
        tabs.addTab(tabs.newTab().setText("front"))
        tabs.addTab(tabs.newTab().setText("Done"))
        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                performOnTabClick(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //currently not in use
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                performOnTabClick(tab.position)
            }
        })
    }

    private fun performOnTabClick(position: Int) {
        when (position) {
            0 -> {
                val currentTime = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("hh.mm.ss aa", Locale.getDefault())
                val output = dateFormat.format(currentTime)
                etAnswer.movementMethod = ScrollingMovementMethod()
                etAnswer.append("$output "+"\n")
                etAnswer.setSelection(etAnswer.text.toString().length )
            }
            1 -> {
                etAnswer.append(getString(R.string.i_walked) +" "+ stepCountValue.toString() +" "+ getString(
                        R.string.total_steps
                    ) + "\n")
                etAnswer.setSelection(etAnswer.text.toString().length)
            }
            2 -> {
                val uploadedImagesList =
                    database?.imagesDao()?.getImagesFromDB(dateString) as ArrayList<MeDairyImagesEntity>
                if (uploadedImagesList.size < 5) {
                    IMAGE_MULTIPLE = 5 - uploadedImagesList.size
                    selectImage()
                } else {
                    val alerts = Alert(this@QuestionAnswerActivity)
                    alerts.showLimitExceedAlert()
                }
            }
            3 -> {
                if (itemPosition >= 1) {
                    itemPosition -= 1
                    setDataOnView(mQuestionAnswerList[itemPosition])
                }

            }
            4 -> {
                if (itemPosition < mQuestionAnswerList.size) {
                    itemPosition += 1
                    if (itemPosition == mQuestionAnswerList.size)
                        itemPosition -= 1
                    setDataOnView(mQuestionAnswerList[itemPosition])
                }
            }
            5 -> {
                addAnswers()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            tvDate.setTextColor(colorSelected)
        }
        running = true
        val countSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (countSensor != null) {
            sManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI)
        }

        if (!imageSet) {
            val imagesList = database?.imagesDao()?.setImages(
                mQuestionAnswerList[itemPosition].uid, mQuestionAnswerList[itemPosition].joined_date
            ) as ArrayList<MeDairyImagesEntity>

            if (imagesList.size > 0) {
                imgView.visibility = View.VISIBLE
                val path = imagesList[imagesList.size - 1]
                if (path != null) {
                    imgView.setImageBitmap(
                        Utilities.getBitmapFromUri(
                            this,
                            Uri.parse(imagesList[0].imagesPath)
                        )
                    )
                }
            } else {
                imgView.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
    }

    private fun addAnswers() {
        questionAnswerPresenter.updateAnswers(
            etAnswer.text.toString(),
            tvQuestion.text.toString(),
            dateString
        )
    }

    @SuppressLint("InflateParams")
    private fun setTabIcons() {
        tabOne = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabOne.setImageResource(R.drawable.clock)
        tabs.getTabAt(0)?.customView = tabOne
        tabTWo = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabTWo.setImageResource(R.drawable.running)
        tabs.getTabAt(1)?.customView = tabTWo
        tabThree = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabThree.setImageResource(R.drawable.camera)
        tabs.getTabAt(2)?.customView = tabThree
        tabFour = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabFour.setImageResource(R.drawable.back_arrow)
        tabs.getTabAt(3)?.customView = tabFour
        tabFive = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabFive.setImageResource(R.drawable.front_arrow)
        tabs.getTabAt(4)?.customView = tabFive
    }

    private fun wordCount(line: String): Long {
        var numWords: Long = 0
        var index = 0
        var prevWhiteSpace = true
        while (index < line.length) {
            val c = line[index++]
            val currWhiteSpace = Character.isWhitespace(c)
            if (prevWhiteSpace && !currWhiteSpace) {
                numWords++
            }
            prevWhiteSpace = currWhiteSpace
        }
        return numWords
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }


    @SuppressLint("SetTextI18n")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s != null) {
            val aList = s.split(" ")

            var splittedString = ""
            for (value in aList) {
                splittedString += value
                characterLength = splittedString.replace("\n","") }
            System.out.println("vghjb" + splittedString)
                if (characterLength!!.length > 0 && characterLength!!.isNotEmpty()) {
                    tvCharacters.text = "${characterLength?.length} characters, "
                } else {
                    tvCharacters.text = "${characterLength?.length} character, "
                }
                tvWords.text = "${wordCount(s.toString().trim())} words"

        }
    }


    private fun getMarshmallowPermission(permissionRequest: String, requestCode: Int): Boolean {
        return Utilities.checkPermission(
            this@QuestionAnswerActivity,
            permissionRequest,
            requestCode
        )
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.choose_from_gallery),
            "Cancel"
        )
        val builder = AlertDialog.Builder(this@QuestionAnswerActivity)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            if (items[item] == getString(R.string.take_photo)) {
                userChoosenTask = getString(R.string.take_photo)
                val resultCamera = getMarshmallowPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Utilities.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
                if (resultCamera)
                    cameraIntent()
            } else if (items[item] == getString(R.string.choose_from_gallery)) {
                userChoosenTask = getString(R.string.choose_from_gallery)
                val resultGallery = getMarshmallowPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Utilities.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                if (resultGallery)
                    galleryIntent()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), IMAGE_MULTIPLE)
    }

    private fun cameraIntent() {
        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Folder/"
        val newDir = File(dir)
        newDir.mkdirs()
        val file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString() + ".jpg"
        val newFile = File(file)
        try {
            newFile.createNewFile()
        } catch (e: IOException) {
            Log.e("Me Diary", "file: $e")
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            imageUri =
                FileProvider.getUriForFile(
                    applicationContext,
                    BuildConfig.APPLICATION_ID + ".provider",
                    newFile
                )
        } else {
            imageUri = Uri.fromFile(newFile)
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUEST_CAMERA)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK && null != data) {
            imageSet = true
            if (data.data != null) {
                imageUri = data.data
                storeSelectedImagesInDb(imageUri!!)
            } else if (data.clipData != null) {
                val count = data.clipData!!.itemCount
                if (count <= IMAGE_MULTIPLE) {
                    for (i in 0 until count) {
                        imageUri = data.clipData!!.getItemAt(i).uri
                        storeSelectedImagesInDb(imageUri!!)
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.plaese_select_max) + IMAGE_MULTIPLE + getString(R.string.images_for_this_date),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            imageSet = true
            storeSelectedImagesInDbCaptureFromCamera(imageUri!!)
        }
    }

    private fun storeSelectedImagesInDb(uri: Uri) {
        bm = Utilities.getBitmapFromUri(this, uri)
        imgView.visibility = View.VISIBLE
        imgView.setImageBitmap(Utilities.getBitmapFromUri(this, Uri.parse(/*path.imagesPath*/imagesList[0].imagesPath)))
        val imagesUri = Utilities.getImageUriFromBitmap(this, bm!!)
        val imagesData = MeDairyImagesEntity(
            mQuestionAnswerList[itemPosition].uid,
            mQuestionAnswerList[itemPosition].joined_date,
            imagesUri.toString()
        )
        database?.imagesDao()?.insertImagesIntoDB(imagesData)
    }

    private fun storeSelectedImagesInDbCaptureFromCamera(uri: Uri) {
        bm = Utilities.getBitmapFromUri(this, uri)
        val inputStream = contentResolver.openInputStream(uri)
        val ei =
            if (android.os.Build.VERSION.SDK_INT > 24) ExifInterface(inputStream) else ExifInterface(
                uri.path
            )
        val orientation: Int
        orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val rotateImage: Bitmap = bm!!
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> bm = Utilities.rotateMyImage(bm!!, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> bm = Utilities.rotateMyImage(bm!!, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> bm = Utilities.rotateMyImage(bm!!, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bm = bm
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }

        imgView.visibility = View.VISIBLE
        imgView.setImageBitmap(rotateImage)
        val imageUri = Utilities.getImageUriFromBitmap(this, rotateImage)
        val imagesData = MeDairyImagesEntity(
            mQuestionAnswerList[itemPosition].uid,
            mQuestionAnswerList[itemPosition].joined_date,
            imageUri.toString()
        )
        database?.imagesDao()?.insertImagesIntoDB(imagesData)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Utilities.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (getMarshmallowPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Utilities.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                )
                    cameraIntent()
            }
            Utilities.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryIntent()
            }
        }
    }

}