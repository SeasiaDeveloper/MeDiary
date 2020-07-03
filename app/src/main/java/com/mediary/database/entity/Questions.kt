package com.mediary.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mediary.database.Converters
import java.io.Serializable

@Entity(tableName = "questionsTable")
class Questions(
    var question: String,
    var answers: String,
    @TypeConverters(Converters::class)
    var joined_date: String,
    var questionId: Int,
    var isHidden: Boolean,
    var isSelected: Boolean,
    @TypeConverters(Converters::class)
    var currentTime:String,
    val categoryId: Int,
    val categoryName: String,
    val isImages: String,
    val isDeleted:Boolean
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

