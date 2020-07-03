package com.mediary.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mediary.database.Converters
import java.io.Serializable

@Entity(tableName = "imagesTable")
data class MeDairyImagesEntity (
    val imagesId: Int,
    @TypeConverters(Converters::class)
    val insertedDate: String,
    val imagesPath: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}