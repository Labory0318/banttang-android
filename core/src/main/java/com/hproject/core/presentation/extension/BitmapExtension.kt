package com.hproject.core.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Bitmap 회전
 *
 * @author hjkim
 * @since 2022/10/02
 **/
fun Bitmap.rotate(context: Context, uri: Uri): Bitmap? {
    var rotateBitmap: Bitmap? = null

    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val exif = ExifInterface(inputStream!!)
        inputStream.close()

        val exifAttr = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (exifAttr != -1) {
            rotateBitmap = when (exifAttr) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270f)
                ExifInterface.ORIENTATION_NORMAL -> this
                else -> this
            }
        }
    } catch (e : IOException) {
        e.printStackTrace()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    return rotateBitmap
}

/**
 * bitmap 회전
 *
 * @author hjkim
 * @since 2022/10/02
 **/
private fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source!!, 0, 0, source.width, source.height,
        matrix, true)
}