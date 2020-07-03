package com.mediary.activities

import android.content.Context
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediary.R
import com.mediary.adapters.PromptLibraryAdapter
import com.mediary.base.BaseActivity
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions
import com.mediary.listeners.OnSelectCategoryPosition
import com.mediary.utils.*
import kotlinx.android.synthetic.main.activity_prompt_library.*
import kotlinx.android.synthetic.main.activity_prompt_library.toolbar


class PromptLibraryActivity : BaseActivity(), Toolbar.OnMenuItemClickListener,
    OnSelectCategoryPosition {
    private lateinit var recyclerAdapter: PromptLibraryAdapter
    private lateinit var dateString: String
    private lateinit var mQuestionAnswerList: List<Questions>
    private var monthYear: String? = null
    private var date: String? = null
    override fun getLayout(): Int {
        return R.layout.activity_prompt_library
    }

    override fun setupUI() {
        val layoutManager = LinearLayoutManager(this@PromptLibraryActivity)
        date = intent.getStringExtra(Constants.DATE)
        monthYear = intent.getStringExtra(Constants.YEAR)
        dateString = Utilities.dbDateFormat(date!!)
        if (database != null) {
            mQuestionAnswerList = database!!.questionsDao().getAllSelectedQuestions(dateString)
            rvPromptItems.layoutManager = layoutManager
            recyclerAdapter = PromptLibraryAdapter(mQuestionAnswerList, database!!,this)
            rvPromptItems.adapter = recyclerAdapter
        }
        toolbar.title = getString(R.string.prompt_library)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.inflateMenu(R.menu.add_category)
        toolbar.setOnMenuItemClickListener(this)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun handleKeyboard(): View {
        return parentPrompat
    }

    fun tintMenuIcon(context: Context, item: MenuItem, @ColorRes color: Int) {
        val normalDrawable = item.icon
        val wrapDrawable = DrawableCompat.wrap(normalDrawable)
        DrawableCompat.setTint(wrapDrawable, context.resources.getColor(color))
        item.icon = wrapDrawable
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.addCategory) {
            val alerts = Alert(this)
            alerts.addCategoryAlert(this, this@PromptLibraryActivity)
        }
        return false
    }


    override fun onCategoryClick(selectedCategory: Int, categoryName: String, category: String) {
        //
        val promptQuestion = PromptQuestion(
            category, Utilities.setCurrentDate(), selectedCategory
            /*1*/, categoryName
        )
        val id = database?.questionsDao()?.insertCategoryPromptQuestions(promptQuestion)
        //val promptQuestions = database!!.questionsDao().getAllQuestionsData()
        val questions = Questions(
            category,
            "",
            dateString,
            id!!.toInt(),
            true,
            true,
            "",
            selectedCategory,
            categoryName,
            "false",
            false
        )
        database!!.questionsDao().insert(questions)
        mQuestionAnswerList = database!!.questionsDao().getAllSelectedQuestions(dateString)
        recyclerAdapter.setList(mQuestionAnswerList)
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        val colorSelected =
            PreferenceHandler.readInteger(this, PreferenceHandler.COLOR_SELECTION, 0)
        if (colorSelected != 0) {
            toolbar.setBackgroundColor(colorSelected)
        }
    }
}