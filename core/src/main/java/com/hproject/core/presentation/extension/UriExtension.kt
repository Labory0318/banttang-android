package com.hproject.core.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

/**
 * @author hsjun
 * @since 2022/03/09
 */
fun Uri.resize(context: Context, resize: Int): Bitmap? {
    var resizedBitmap: Bitmap? = null
    val options: BitmapFactory.Options = BitmapFactory.Options()

    try {
        var width = options.outWidth
        var height = options.outHeight
        var sampleSize = 1

        while (true) {
            if (width / 2 < resize || height / 2 < resize) break
            width /= 2
            height /= 2
            sampleSize *= 2
        }
        options.inSampleSize = sampleSize
        resizedBitmap = BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(this),
            null,
            options
        )
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    return resizedBitmap
}