package com.hproject.banttang.presentation.common.extension

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.hproject.banttang.presentation.common.dialog.AlertDialogBuilder

/**
 * common alert dialog
 *
 * @author hjkim
 * @since 2023/03/23
 **/
fun Fragment.alertDialog(
    title: CharSequence? = null,
    message: CharSequence,
    cancelable: Boolean = true,
    builder: AlertDialogBuilder.() -> Unit
) = AlertDialogBuilder(
    context = requireContext(),
    title = title,
    message = message,
    cancelable = cancelable
).apply {
    builder()
}.create().show()

/**
 * common alert dialog
 *
 * @author hjkim
 * @since 2023/03/23
 **/
fun Fragment.alertDialog(
    @StringRes title: Int? = null,
    @StringRes message: Int,
    cancelable: Boolean = true,
    builder: AlertDialogBuilder.() -> Unit
) = alertDialog(
    title = title?.let { getString(title) },
    message = getString(message),
    cancelable = cancelable,
    builder = builder
)