package com.mediary.database

import androidx.room.*
import com.mediary.database.entity.PromptQuestion
import com.mediary.database.entity.Questions
import java.util.*

@Dao
interface QuestionsDao {
    @Insert
    fun insert(questionModel: Questions)

    @Insert
    fun insertPromptQuestions(questionModel: List<PromptQuestion>)

    @Insert
    fun insertCategoryPromptQuestions(questionModel: PromptQuestion):Long

    @Query("SELECT * FROM promptQuestionsTable")
    fun getAllQuestionsData(): List<PromptQuestion>

    @Query("SELECT * FROM questionsTable WHERE joined_date=:date ORDER BY categoryId")
    fun getAllSelectedQuestions(date: String): List<Questions>

    @Query("SELECT * FROM promptQuestionsTable")
    fun getQuestionsDate(): PromptQuestion

    @Query("SELECT * FROM promptQuestionsTable")
    fun getAllPromptQuestionsData(): List<PromptQuestion>

    @Query("SELECT * FROM questionsTable WHERE joined_date=:date and isHidden=:hiddenData and isSelected=:selectedData and isDeleted=:isDeleted")
    fun getAllQuestions(date: String, hiddenData: Boolean, selectedData: Boolean,isDeleted:Boolean): List<Questions>


    @Query("SELECT * FROM questionsTable WHERE joined_date=:date")
    fun getAllQuestionsAccordingToDate(date: String): List<Questions>

    @Update
    fun updateQuestionsWithAnswers(data: Questions)

    @Query("UPDATE QUESTIONSTABLE SET answers = :answers WHERE question =:question and joined_date=:date")
    fun update(answers: String?, question: String?,date: String)

    @Query("UPDATE QUESTIONSTABLE SET question = :question WHERE joined_date=:date and uid=:uid" )
    fun updateEditedQuestion(question: String?, date: String, uid: Int)

    @Query("UPDATE  QUESTIONSTABLE SET isHidden = :isHidden, isDeleted=:isDeleted WHERE uid =:uId")
    fun deletedQuestions(isHidden: Boolean ,isDeleted:Boolean, uId: Int?)

    @Query("UPDATE  QUESTIONSTABLE SET isHidden = :isHidden, isSelected = :isSelected, isDeleted=:isDeleted WHERE uid =:uId")
    fun updateQuestions(isHidden: Boolean, isSelected: Boolean, isDeleted:Boolean, uId: Int?)

    @Query("UPDATE  QUESTIONSTABLE SET  isSelected = :isSelected WHERE uid =:uId")
    fun updatePromptQuestions( isSelected: Boolean,uId: Int?)

    @Query("SELECT * FROM questionsTable where isSelected=:isSelected and joined_date=:date")
      fun getPromptQuestionsCount(isSelected: Boolean, date: String):List<Questions>

    @Query("DELETE FROM questionsTable where uid = :questionsId and joined_date =:date")
    fun deleteQuestion(questionsId: Int, date: String)

    @Query("UPDATE QUESTIONSTABLE SET answers = :answers, currentTime= :time WHERE joined_date =:date")
    fun updateDailyData(answers: String?, date: String?, time: String?)

    @Query("UPDATE promptQuestionsTable SET insertedDate = :insertedDate WHERE question =:question")
    fun updateInsertedDate(insertedDate: Date?, question: String?)

    @Query("DELETE FROM imagesTable where imagesId = :imagesId and insertedDate =:date")
    fun deleteImgDailyData(imagesId: Int, date: String)

    @Query("select promptQuestionsTable.* From promptQuestionsTable left join questionsTable ON (promptQuestionsTable.id = questionsTable.questionId) where questionsTable.questionId IS null")
    fun insertNewQuestions(): PromptQuestion

    @Query("SELECT * FROM questionsTable WHERE joined_date=:date and isHidden=:hiddenData and isSelected=:selectedData")
    fun getQuestion(date: String, hiddenData: Boolean, selectedData: Boolean): Questions

    @Query("SELECT * FROM questionsTable WHERE joined_date=:date  and isSelected=:selectedData")
    fun getPromptQuestion(date: String, selectedData: Boolean): Questions

    @Query("SELECT * FROM questionsTable WHERE joined_date=:date and isHidden=:hiddenData and isSelected=:selectedData and isDeleted=:isDeleted")
    fun getQuestionFromPrompt(date: String, hiddenData: Boolean, selectedData: Boolean,isDeleted:Boolean): Questions
}