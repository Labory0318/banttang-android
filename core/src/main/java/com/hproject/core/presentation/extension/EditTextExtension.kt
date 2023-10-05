package com.hproject.core.presentation.extension

import android.widget.EditText

/**
 * 커서 끝으로 이동
 *
 * @author thomas
 * @since 2022/11/08
 **/
fun EditText.moveCursorToEnd() {
    post {
        setSelection(length())
    }
}