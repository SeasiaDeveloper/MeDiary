package com.mediary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mediary.database.entity.WeatherMoodTable

@Dao
interface WeatherMoodDao {
    @Insert
    fun insertWeatherOption(questionModel: WeatherMoodTable)

    @Query("SELECT * FROM emojiWeatherTable")
    fun getMoodData(): List<WeatherMoodTable>

    @Query("SELECT type FROM emojiWeatherTable WHERE date= :date AND weatherMood= :mood")
    fun getMoodTypeByDate(date: String?,mood: String?): String?

    @Query("DELETE FROM emojiWeatherTable WHERE date=:date")
    fun deleteEmojis(date: String):Int

    @Query("SELECT * FROM emojiWeatherTable WHERE weatherMood= :mood")
    fun getMoodType(mood: String?): List<WeatherMoodTable>

    @Query("UPDATE emojiWeatherTable SET type = :type WHERE date =:date AND weatherMood= :weatherMood")
    fun updateMoodOption(type: String?, weatherMood: String?, date: String?):Int

    @Query("UPDATE emojiWeatherTable SET type = :type, weatherMood= :weatherMood WHERE date =:date")
    fun updateWeatherOption(type: String?, weatherMood: String?, date: String?):Int

}