package com.hproject.core.presentation.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {
    private var mToast: Toast? = null

    /**
     * 토스트 메세지 출력
     *
     * @param context       context
     * @param message       출력 메세지
     * @param isLongToast   [Toast.LENGTH_LONG] 여부 (default: false)
     * @author thomas
     * @since 2022/11/03
     **/
    @JvmStatic
    fun showToast(
        context: Context?,
        message: String,
        isLongToast: Boolean = false
    ) {
        cancelShowingToast()

        val duration: Int = if (isLongToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        mToast = Toast.makeText(context, message, duration)
        mToast?.show()
    }

    /**
     * 토스트 메세지 출력
     *
     * @param context       context
     * @param messageResId  출력 메세지
     * @param isLongToast   [Toast.LENGTH_LONG] 여부 (default: false)
     * @author thomas
     * @since 2022/11/03
     **/
    @JvmStatic
    fun showToast(
        context: Context?,
        @StringRes messageResId: Int,
        isLongToast: Boolean = false
    ) {
        context?.let {
            showToast(
                context = it,
                message = it.getString(messageResId),
                isLongToast = isLongToast
            )
        }
    }

    /**
     * 출력 중인 토스트 취소
     *
     * @author thomasㅈ
     * @since 2022/11/03
     **/
    @JvmStatic
    fun cancelShowingToast() {
        mToast?.cancel()
    }
}