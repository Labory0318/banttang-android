package com.hproject.banttang.presentation.common.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.hproject.banttang.databinding.AlertDialogBinding

class AlertDialogBuilder(
    context: Context,
    title: CharSequence?,
    message: CharSequence?,
    private val cancelable: Boolean = true
) {
    private val binding = AlertDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.titleTextView.text = title
        binding.messageTextView.text = message
    }

    private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context).setView(binding.root)

    private var dialog: AlertDialog? = null

    fun positiveButton(@StringRes textResource: Int, func: (() -> Unit)? = null) {
        with(binding.positiveButton) {
            text = context.getString(textResource)
            setClickListenerToDialogButton(func)
        }
    }

    fun positiveButton(text: CharSequence, func: (() -> Unit)? = null) {
        with(binding.positiveButton) {
            this.text = text
            setClickListenerToDialogButton(func)
        }
    }

    fun negativeButton(@StringRes textResource: Int, func: (() -> Unit)? = null) {
        with(binding.negativeButton) {
            text = context.getString(textResource)
            setClickListenerToDialogButton(func)
        }
    }

    fun negativeButton(text: CharSequence, func: (() -> Unit)? = null) {
        with(binding.negativeButton) {
            this.text = text
            setClickListenerToDialogButton(func)
        }
    }

    fun onDismiss(func: (() -> Unit)? = null) {
        dialogBuilder.setOnDismissListener { func?.invoke() }
    }


    fun onCancel(func: () -> Unit) {
        dialogBuilder.setOnCancelListener { func() }
    }

    fun create(): AlertDialog {
        binding.titleTextView.goneIfTextEmpty()
        binding.messageTextView.goneIfTextEmpty()
        binding.positiveButton.goneIfTextEmpty()
        binding.negativeButton.goneIfTextEmpty()

        return dialogBuilder
            .setCancelable(cancelable)
            .create().also {
                it.window?.setBackgroundDrawableResource(android.R.color.transparent)

                dialog = it
            }
    }

    private fun TextView.goneIfTextEmpty() {
        isVisible = !text.isNullOrEmpty()
    }

    private fun Button.setClickListenerToDialogButton(func: (() -> Unit)?) {
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }
    }
}