package com.mediary.activities.editquestions

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mediary.R
import com.mediary.activities.editquestions.presenter.EditQuestionsPresenter
import com.mediary.activities.editquestions.presenter.EditQuestionsPresenterImpl
import com.mediary.activities.editquestions.view.EditQuestionsView
import com.mediary.adapters.EditQuestionsAdapter
import com.mediary.base.BaseActivity
import com.mediary.customviews.CustomEditText
import com.mediary.database.entity.Questions
import com.mediary.listeners.OnDeleteItemClickListener
import com.mediary.listeners.OnDeleteQuestionClickListener
import com.mediary.listeners.OnUpdateQuestionClickListener
import com.mediary.listeners.OnUpdatedItemClickListener
import com.mediary.utils.Alert
import com.mediary.utils.Constants
import com.mediary.utils.PreferenceHandler
import com.mediary.utils.Utilities
import kotlinx.android.synthetic.main.activity_edit_questions.*
import kotlinx.android.synthetic.main.activity_questionanswers.*


class EditQuestionActivity : BaseActivity(), EditQuestionsView, OnDeleteQuestionClickListener,
    OnDeleteItemClickListener, OnUpdatedItemClickListener, OnUpdateQuestionClickListener {

    private lateinit var questionList: Questions
    private lateinit var editedQuestion: String
    private lateinit var dateString: String
    private var date: String? = null
    private var recyclerAdapter: EditQuestionsAdapter? = null
    private lateinit var mQuestionAnswerList: MutableList<Questions>
    private lateinit var tabOne: AppCompatImageView
    private lateinit var tabTWo: AppCompatImageView
    private lateinit var tabThree: AppCompatImageView
    private lateinit var tabFour: AppCompatImageView
    private lateinit var editQuestionsPresenter: EditQuestionsPresenter
    private var flagDecoration: Boolean = false

    override fun getLayout(): Int {
        return R.layout.activity_edit_questions
    }

    override fun setupUI() {
        if (database != null) {
            editQuestionsPresenter = EditQuestionsPresenterImpl(this, database!!)
        }
        date = intent.getStringExtra(Constants.DATE)
        if (date != null) {
            toolbarTv.text = date
            dateString = Utilities.dbDateFormat(date!!)
        }
        val layoutManager = GridLayoutManager(this@EditQuestionActivity, 2)
        editQuestionsRV.layoutManager = layoutManager
        if (Constants.isColorButtonClicked) {
            val colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
            editQuestionTabs.setBackgroundColor(colorSelected)
            toolbarTv.setTextColor(colorSelected)
        }else{
            editQuestionTabs.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }
        setTabLayout()
        setTabIcons()
        if (Constants.isColorButtonClicked) {
            val colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
            editQuestionTabs.setBackgroundColor(colorSelected)
        }else{
            editQuestionTabs.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }
    }

    override fun onDeleteClick(position: Int, question: Questions) {
        if (mQuestionAnswerList.size == 1) {
            val alerts = Alert(this)
            alerts.showNotEmptyGridAlert()
        } else {
            val alerts = Alert(this)
            alerts.showDeleteAlert(this, position, question)
        }
    }

    override fun onUpdateEditQuestion(position: Int, question: Questions, txtQuestions: String) {
        editedQuestion = txtQuestions
        questionList = question
    }

    override fun onItemClick(position: Int, question: Questions) {
        database?.questionsDao()?.deletedQuestions(true, true, question.uid)
        setAdapter()
    }

    private fun addQuestions() {
        editQuestionsPresenter.updateQuestionsList(dateString)
        addAnalytics("addQuestion")
    }

    private fun setAdapter() {
        mQuestionAnswerList = database!!.questionsDao().getAllQuestions(
            dateString,
            false,
            true,
            false
        ) as MutableList<Questions>
        if (mQuestionAnswerList.size < 1 || mQuestionAnswerList.size == 0) {
            val list = database!!.questionsDao().getAllQuestionsAccordingToDate(dateString)
            if (list.isNotEmpty()) {
                val question = list[0]
                mQuestionAnswerList.add(question)
            }
            if (recyclerAdapter == null) {
                recyclerAdapter = EditQuestionsAdapter(mQuestionAnswerList, this, this)
                editQuestionsRV.adapter = recyclerAdapter
            } else {
                recyclerAdapter?.setList(mQuestionAnswerList)
                recyclerAdapter?.notifyDataSetChanged()
            }
            if (mQuestionAnswerList.size == 1) {
                if (!flagDecoration) {
                    editQuestionsRV.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        } else {
            if (!flagDecoration) {
                editQuestionsRV.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    )
                )
                editQuestionsRV.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                flagDecoration = true
            }

            if (recyclerAdapter == null) {
                recyclerAdapter = EditQuestionsAdapter(mQuestionAnswerList, this, this)
                editQuestionsRV.adapter = recyclerAdapter
            } else {
                recyclerAdapter?.setList(mQuestionAnswerList)
                recyclerAdapter?.notifyDataSetChanged()

            }
        }
    }

    private fun setTabLayout() {
        editQuestionTabs.addTab(editQuestionTabs.newTab().setText("cross"))
        editQuestionTabs.addTab(editQuestionTabs.newTab().setText("add"))
        editQuestionTabs.addTab(editQuestionTabs.newTab().setText("shuffle"))
        editQuestionTabs.addTab(editQuestionTabs.newTab().setText("done"))
        editQuestionTabs.tabMode = TabLayout.MODE_FIXED
        editQuestionTabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
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
                finish()
            }
            1 -> {
                addQuestions()
            }
            2 -> {
                mQuestionAnswerList.shuffle()
                editQuestionsRV.swapAdapter(recyclerAdapter, false)
            }
            3 -> {
                finish()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setTabIcons() {
        tabOne = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabOne.setImageResource(R.drawable.cross)
        tabOne.setColorFilter(ContextCompat.getColor(this, R.color.white))
        editQuestionTabs.getTabAt(0)?.customView = tabOne
        tabTWo = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabTWo.setImageResource(R.drawable.plus)
        tabTWo.setColorFilter(ContextCompat.getColor(this, R.color.white))
        editQuestionTabs.getTabAt(1)?.customView = tabTWo
        tabThree = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabThree.setImageResource(R.drawable.shuffle)
        tabThree.setColorFilter(ContextCompat.getColor(this, R.color.white))
        editQuestionTabs.getTabAt(2)?.customView = tabThree
        tabFour = layoutInflater.inflate(R.layout.custom_tab, null) as AppCompatImageView
        tabFour.setImageResource(R.drawable.checked)
        tabFour.setColorFilter(ContextCompat.getColor(this, R.color.white))
        editQuestionTabs.getTabAt(3)?.customView = tabFour

    }

    override fun showAddedQuestionsList() {
        setAdapter()
    }

    override fun showUpdatedQuestions() {
        setAdapter()
    }

    override fun showPromptAlert() {
        val alerts = Alert(this)
        alerts.showNoPromptAlert()
    }

    override fun showEmptyPromptAlert() {
        val alerts = Alert(this)
        alerts.showNoPromptAlert()   }


    override fun handleKeyboard(): View {
        return editQuestionParent
    }

    override fun onResume() {
        setAdapter()
        super.onResume()
        if (Constants.isColorButtonClicked) {
            val colorSelected = PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
            editQuestionTabs.setBackgroundColor(colorSelected)
            toolbarTv.setTextColor(colorSelected)
        }else{
            editQuestionTabs.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }
    }

    override fun onUpdatedItemClick(position: Int, question: Questions) {
        val alerts = Alert(this)
        alerts.showUpdatedQuestionAlert(this, position, question)
    }

    override fun onUpdatedQuestionClick(position: Int, questionText: String, question: Questions) {
        editQuestionsPresenter.updatedEditedQuestion(questionText, dateString, question.uid)
        setAdapter()
    }
}