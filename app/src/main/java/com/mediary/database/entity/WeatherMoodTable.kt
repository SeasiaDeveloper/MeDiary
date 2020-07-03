package com.mediary.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mediary.database.Converters
import java.io.Serializable

@Entity(tableName = "emojiWeatherTable")
data class WeatherMoodTable(
    var type: String,
    val weatherMood: String,
    @TypeConverters(Converters::class)
    val date: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}