package com.mediary.utils

import android.content.Context
import android.content.Intent
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import android.content.Intent.ACTION_SEND
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.text.format.DateFormat
import com.mediary.database.entity.MeDairyImagesEntity
import java.io.File
import java.io.IOException
import java.net.URLConnection
import java.util.*
import androidx.core.content.FileProvider
import com.itextpdf.text.Paragraph
import com.mediary.BuildConfig
import com.mediary.database.AppDatabase
import com.mediary.database.entity.Questions


object ImagesUtil {
    private lateinit var imagesList: List<MeDairyImagesEntity>
    private lateinit var file: String
    private lateinit var newfile: File

    fun getPdfFromImages(context: Context, questionsAnswersList: List<Questions>, database: AppDatabase, dateString:String) {
        val document = Document()
        val directoryPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Folder/"
        val newDir = File(directoryPath)
        newDir.mkdirs()
        file = directoryPath + DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString() + ".pdf"
        newfile = File(file)
        try {
            newfile.createNewFile()
        } catch (e: IOException) {
        }

        PdfWriter.getInstance(document, FileOutputStream(newfile)) //  Change pdf's name.
        document.open()
        for (i in questionsAnswersList.indices) {
            val questionsData = questionsAnswersList[i]
            val questions = questionsAnswersList[i].question
            val answers = questionsAnswersList[i].answers
             imagesList = database.imagesDao().setImages(questionsData.uid, questionsData.joined_date)
            if(!TextUtils.isEmpty(questionsAnswersList[i].answers) || (imagesList !=null && imagesList.isNotEmpty())){
                document.add(Paragraph(questions))
            }
            if(!TextUtils.isEmpty(questionsAnswersList[i].answers)){
                document.add(Paragraph(answers))}

            if(imagesList !=null && imagesList.isNotEmpty()) {
                for (j in imagesList.indices) {
                    val uri = Uri.parse(imagesList[j].imagesPath)
                    val image = Image.getInstance(getRealPathFromURI(context, uri))  // Change image's name and extension.
                    val scaler = (document.pageSize.width - document.leftMargin()
                            - document.rightMargin() - 0) / image.width * 100 // 0 means you have no indentation. If you have any, change it.
                    image.scalePercent(scaler)
                    image.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
                    document.add(image)
                }
            }
        }
        document.add( Paragraph("Hello World!"))
        document.close()
    }


    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    fun shareFile(context: Context) {
        val intentShareFile = Intent(ACTION_SEND)
        intentShareFile.type = URLConnection.guessContentTypeFromName(newfile.name)
        val imageUri: Uri
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            imageUri =
                FileProvider.getUriForFile(
                    context.applicationContext,
                    BuildConfig.APPLICATION_ID + ".provider",
                    newfile
                )
        } else {
            imageUri = Uri.fromFile(newfile)
        }

        intentShareFile.putExtra(
            Intent.EXTRA_STREAM,
            imageUri
        )
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(intentShareFile, "Share File"))
    }

}