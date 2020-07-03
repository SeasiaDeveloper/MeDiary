package com.mediary.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.mediary.R
import com.mediary.database.entity.Questions
import com.mediary.listeners.*

class Alert(private var context: Activity) {
    private lateinit var selectedCategory: String
    private var item: Int = 0
    private var builder: AlertDialog.Builder = AlertDialog.Builder(context)

    /**
     * Show delete alert
     */
    fun showDeleteAlert(
        listener: OnDeleteItemClickListener,
        position: Int,
        question: Questions
    ) {
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(context.getString(R.string.sure_to_delete))
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            listener.onItemClick(position, question)
        }
        builder.setNegativeButton(
            "No"
        ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Show updatedQuestion alert
     */
    fun showUpdatedQuestionAlert(
        listener: OnUpdateQuestionClickListener,
        position: Int,
        question: Questions
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.question_updated_dialog, null)
        dialogBuilder.setView(dialogView)
        val editText = dialogView.findViewById(R.id.etQuestion) as EditText
        val ivCancel = dialogView.findViewById(R.id.imgCancel) as ImageView
        val ivDone = dialogView.findViewById(R.id.imgDone) as ImageView
        editText.setText(question.question)
        val alertDialog = dialogBuilder.create()
        ivCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        ivDone.setOnClickListener {
            listener.onUpdatedQuestionClick(position, editText.text.toString(), question)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun showCustomizeColorAlert(
        listener: OnCustomizeColorClickListener
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.change_color_dialog, null)
        dialogBuilder.setView(dialogView)
        val txtYes = dialogView.findViewById(R.id.tvYes) as TextView
        val txtNo = dialogView.findViewById(R.id.tvOkay) as TextView
        val txtDontRequired = dialogView.findViewById(R.id.tvDontRequired) as TextView
        val alertDialog = dialogBuilder.create()
        txtYes.setOnClickListener {
            listener.onColorChangeClick()
            alertDialog.dismiss()
        }
        txtNo.setOnClickListener {
            alertDialog.dismiss()
        }
        txtDontRequired.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    fun showCustomizeOtherAppsAlert(listener: OnItemSelected) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.other_apps_dialog, null)
        dialogBuilder.setView(dialogView)
        val lineareasyLedger = dialogView.findViewById(R.id.lineareasyLedger) as LinearLayout
        val linearsantsinghmaskeen = dialogView.findViewById(R.id.linearsantsinghmaskeen) as LinearLayout
        val lineardrWater = dialogView.findViewById(R.id.lineardrWater) as LinearLayout
        val linearAlarmCal = dialogView.findViewById(R.id.linearAlarmCal) as LinearLayout
        val ivCancel = dialogView.findViewById(R.id.ivCancel) as ImageView
        val alertDialog = dialogBuilder.create()
        lineareasyLedger.setOnClickListener {
            listener.onItemClick("EasyLedger")
            alertDialog.dismiss()
        }
        linearsantsinghmaskeen.setOnClickListener {
            listener.onItemClick("SantSinghMaskeen")
            alertDialog.dismiss()
        }
        lineardrWater.setOnClickListener {
            listener.onItemClick("DrWater")
            alertDialog.dismiss()
        }

        linearAlarmCal.setOnClickListener {
            listener.onItemClick("AlarmCalender")
            alertDialog.dismiss()
        }
        ivCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }



    /**
     * Show addCategory alert
     */
    fun addCategoryAlert(
        listener: OnSelectCategoryPosition,
        context: Activity
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_category_dialog, null)
        dialogBuilder.setView(dialogView)
        val editText = dialogView.findViewById(R.id.etPromptQuestion) as EditText
        val ivCancel = dialogView.findViewById(R.id.imgCancel) as ImageView
        val ivDone = dialogView.findViewById(R.id.imgDone) as ImageView
        val colors = arrayOf<String>(
            "Category",
            "Special Day",
            "Health",
            "Social interactions",
            "Career Development",
            "Financial Independence",
            "Family Happiness",
            "Personal Development"
        )
        val spinner = dialogView.findViewById(R.id.spCategory) as Spinner
        val spinnerArrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, colors)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                item = parent.selectedItemPosition
                selectedCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        val alertDialog = dialogBuilder.create()
        ivCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        ivDone.setOnClickListener {
            if (selectedCategory.isNotEmpty()) {
                Utilities.showMessage(context, "Please select category")
            } else if(editText.text.toString().isNotEmpty()){
                Utilities.showMessage(context, "Please enter question")
            } else{
                listener.onCategoryClick(item, selectedCategory, editText.text.toString())
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    fun showDailyDeleteDataAlert(listener: OnDeleteDailyDataClickListener, date: String) {
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(
            "Are you sure you want to delete your notes for ${Utilities.dbTimeFormat(
                date
            )}?"
        )
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            listener.onDeleteDataClick(date)
        }
        builder.setNegativeButton(
            "No"
        ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    fun showLimitExceedAlert() {
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(context.getString(R.string.limit_exceed))
        builder.setCancelable(false)
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    fun showNoPromptAlert() {
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(context.getString(R.string.no_more_prompts_to_add))
        builder.setCancelable(false)
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    fun showNotEmptyGridAlert() {
        builder.setTitle("Oops!")
        builder.setMessage("Please leave atleast one grid.")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }
}