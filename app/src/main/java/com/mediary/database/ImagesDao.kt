package com.mediary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mediary.database.entity.MeDairyImagesEntity

@Dao
interface ImagesDao {
    @Query("SELECT * FROM IMAGESTABLE WHERE insertedDate=:date")
    fun getImagesFromDB(date: String): List<MeDairyImagesEntity>

    @Insert
    fun insertImagesIntoDB(imagesModel:MeDairyImagesEntity)

    @Query("SELECT * FROM IMAGESTABLE  WHERE imagesId =:uid and insertedDate=:date")
    fun setImages(uid: Int,date: String):List<MeDairyImagesEntity>

    @Query("DELETE FROM IMAGESTABLE WHERE insertedDate=:date")
    fun deleteImages(date: String):Int

    @Query("DELETE FROM IMAGESTABLE WHERE imagesPath=:imagePath")
    fun deleteParticularImage(imagePath: String):Int
}