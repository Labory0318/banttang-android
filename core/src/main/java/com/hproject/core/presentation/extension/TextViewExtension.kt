package com.hproject.core.presentation.extension

import android.text.Spannable
import android.text.SpannableString
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.hproject.core.presentation.component.CenterImageSpan

/**
 * 텍스트 + 이미지 조합
 * - INFO :
 * @author thomas
 * @since 2023/01/18
 **/
fun TextView.setTextWithImages(
    @StringRes combinedTextRes: Int? = null,
    vararg combinedImagesRes: Int
) {
    val combinedString = SpannableString(combinedTextRes?.let { context.getString(it) } ?: text)
    var imageIndex = 0
    combinedString.forEachIndexed { index, c ->
        if (c == '@') {
            ContextCompat.getDrawable(context, combinedImagesRes[imageIndex])?.let { image ->
                image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
                combinedString.setSpan(CenterImageSpan(image), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            imageIndex++
        }
    }

    text = combinedString
}

