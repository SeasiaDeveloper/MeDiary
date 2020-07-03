package com.mediary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mediary.database.entity.MeDairyImagesEntity
import com.mediary.database.entity.PromptQuestion

import com.mediary.database.entity.Questions
import com.mediary.database.entity.WeatherMoodTable

@Database(
    entities = [Questions::class, PromptQuestion::class, MeDairyImagesEntity::class,WeatherMoodTable::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao
    abstract fun imagesDao(): ImagesDao
    abstract fun weatherMoodDao(): WeatherMoodDao

    companion object {
        private var databaseInstance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            synchronized(AppDatabase::class.java) {
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "experts.database"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return databaseInstance
        }
    }

}