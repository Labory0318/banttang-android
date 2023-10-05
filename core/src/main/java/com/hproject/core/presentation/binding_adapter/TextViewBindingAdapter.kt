package com.hproject.core.presentation.binding_adapter

import android.graphics.*
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.text.HtmlCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.databinding.BindingAdapter

object TextViewBindingAdapter {
    /**
     * TextView htmlText
     *
     * @param htmlText  html text
     * @author thomas
     * @since 2022/12/06
     **/
    @JvmStatic
    @BindingAdapter("htmlText")
    fun TextView.setHtmlText(
        htmlText: String?
    ) {
        text = if (htmlText != null) HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT) else ""
    }

    /**
     * 취소선 표시
     *
     * @author thomas
     * @since 2022/12/08
     **/
    @JvmStatic
    @BindingAdapter("isCancelLineVisible")
    fun TextView.setCancelLine(
        isCancelLineVisible: Boolean = false
    ) {
        paintFlags = if (isCancelLineVisible) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    /**
     * 특정 텍스트 컬러 변경
     *
     * @author thomas
     * @since 2023/01/20
     **/
    @JvmStatic
    @BindingAdapter(
        value = [
            "coloredText",
            "coloredTextColor"
        ],
        requireAll = true
    )
    fun TextView.setTextColor(
        coloredText: String?,
        @ColorInt coloredTextColor: Int
    ) {
        if (text.isNullOrBlank() || coloredText.isNullOrBlank()) {
            return
        }

        val start = text.indexOf(coloredText)
        if (start < 0) {
            return
        }

        text = text.toSpannable().apply {
            set(start, start.plus(coloredText.length), ForegroundColorSpan(coloredTextColor))
        }
    }
}