package com.hproject.core.presentation.binding_adapter

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.hproject.core.extension.formattedNumberToString
import com.hproject.core.extension.toFormattedString

object EditTextBindingAdapter {
    @JvmStatic
    @BindingAdapter("number_formatted_text")
    fun EditText.numberToFormattedString(oldValue: Number?, newValue: Number?) {
        if ((oldValue == null && newValue == null) || oldValue == newValue) {
            return
        }

        setText(newValue.toFormattedString())
        setSelection(text.length)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "number_formatted_text",
        event = "android:textAttrChanged"
    )
    fun EditText.formattedStringToLong(): Long {
        return text.toString().formattedNumberToString()?.toLong() ?: 0L
    }
}