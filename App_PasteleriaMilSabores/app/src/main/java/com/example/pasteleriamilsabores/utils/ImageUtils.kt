package com.example.pasteleriamilsabores.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Pictures")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filename = "IMG_$timeStamp.jpg"
            val file = File(context.filesDir, filename)

            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()

            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Obtiene el recurso de imagen apropiado según el tipo de ruta
     * @param context Contexto de la aplicación
     * @param imagePath Puede ser: "drawable:nombre_recurso", ruta de archivo, URL, o null
     * @return El recurso apropiado para Coil (Int para drawable, File para archivos, String para URL)
     */
    fun getImageUri(context: Context, imagePath: String?): Any? {
        return when {
            imagePath == null || imagePath == "preloaded" -> null
            imagePath.startsWith("drawable:") -> {
                // Es un recurso drawable
                val resourceName = imagePath.substringAfter("drawable:")
                val resourceId = context.resources.getIdentifier(
                    resourceName,
                    "drawable",
                    context.packageName
                )
                if (resourceId != 0) resourceId else null
            }
            imagePath.startsWith("http") -> imagePath // URL externa
            else -> {
                // Archivo local
                val file = File(imagePath)
                if (file.exists()) file else null
            }
        }
    }

    /**
     * Convierte el nombre del archivo a formato de recurso Android
     * Ejemplo: "brownie-sin-gluten.jpg" -> "brownie_sin_gluten"
     */
    fun getDrawableResourceName(fileName: String): String {
        return fileName
            .substringBeforeLast(".")
            .replace("-", "_")
            .lowercase()
    }
}