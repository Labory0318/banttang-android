package com.hproject.core.presentation.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.hproject.core.presentation.utils.ToastUtils

/**
 * 텍스트 클립보드 복사
 *
 * @param text      복사할 텍스트
 * @author thomas
 * @since 2023/01/25
 **/
fun Context.copyToClipboard(text: CharSequence) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
        setPrimaryClip(ClipData.newPlainText("", text))
    }
}

/**
 * 토스트 메세지 출력
 *
 * @param message       출력 메세지
 * @param isLongToast   출력 시간이 긴 토스트 여부 (default: false)
 * @author thomas
 * @since 2022/11/03
 **/
fun Context?.showToast(
    message: String,
    isLongToast: Boolean = false
) = ToastUtils.showToast(
    context = this,
    message = message,
    isLongToast = isLongToast
)

/**
 * 토스트 메세지 출력
 *
 * @param messageResId  출력 메세지
 * @param isLongToast   출력 시간이 긴 토스트 여부 (default: false)
 * @author thomas
 * @since 2022/11/03
 **/
fun Context?.showToast(
    messageResId: Int,
    isLongToast: Boolean = false
) = ToastUtils.showToast(
    context = this,
    messageResId = messageResId,
    isLongToast = isLongToast
)

/**
 * 시스템 세팅 화면으로 이동
 *
 * @author thomas
 * @since 2022/11/04
 **/
fun Context.moveToSystemSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:${applicationContext.packageName}")

    startActivity(intent)
}