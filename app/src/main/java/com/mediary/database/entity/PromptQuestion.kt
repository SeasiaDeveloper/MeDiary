package com.mediary.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mediary.database.Converters
import java.io.Serializable
import java.util.*


@Entity(tableName = "promptQuestionsTable")
data class PromptQuestion(
    val question: String,
    @TypeConverters(Converters::class)
    val insertedDate: Date,
    val categoryId: Int,
    val categoryName: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}