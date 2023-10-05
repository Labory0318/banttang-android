package com.hproject.core.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtils {
    /**
     * 키보드 보임 처리
     *
     * @author hsjun
     * @since 2021-03-04
     */
    fun showKeyboard(editText: EditText) {
        with(editText) {
            requestFocus()

            fun showKeyboardNow() {
                if (isFocused) {
                    post {
                        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                    }
                }
            }

            if (hasWindowFocus()) {
                showKeyboardNow()
            } else {
                viewTreeObserver.addOnWindowFocusChangeListener(object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        if (hasFocus) {
                            showKeyboardNow()
                            viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
            }
        }
    }

    /**
     * 키보드 숨김 처리
     *
     * @author thomas
     * @since 2022/12/14
     **/
    fun hideKeyboard(activity: Activity) {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.let { inputMethodManager ->
            activity.currentFocus?.let { currentFocus ->
                inputMethodManager.hideSoftInputFromWindow(
                    currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )

                currentFocus.clearFocus()
            }
        }
    }
}