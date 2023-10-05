package com.hproject.core.presentation.component

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar
import androidx.annotation.LayoutRes

class ProgressDialog(
    context: Context,
    @LayoutRes contentViewResId: Int? = null
) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        contentViewResId?.let {
            setContentView(it)
        } ?: run {
            setContentView(ProgressBar(context))
        }

        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}