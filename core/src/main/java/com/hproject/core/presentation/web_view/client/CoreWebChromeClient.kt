package com.hproject.core.presentation.web_view.client

import android.annotation.SuppressLint
import android.os.Message
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import com.hproject.core.R
import timber.log.Timber

class CoreWebChromeClient(
    private val onProgressChanged: ((progress: Int) -> Unit)? = null,
    private val onCloseWindow: (() -> Unit)? = null
) : WebChromeClient() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
        val settings = view?.settings
        settings?.run {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
        }

        val transport: WebView.WebViewTransport = resultMsg?.obj as WebView.WebViewTransport
        transport.webView = view
        resultMsg.sendToTarget()

        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }

    override fun onCloseWindow(window: WebView?) {
        super.onCloseWindow(window)

        onCloseWindow?.invoke()
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)

        onProgressChanged?.invoke(newProgress)
    }

    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        view?.context?.let { context ->
            AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    result?.confirm()
                }
                .setCancelable(false)
                .create()
                .show()
        }

        return true
    }

    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        view?.context?.let { context ->
            AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    result?.confirm()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    result?.cancel()
                }
                .setCancelable(false)
                .create()
                .show()
        }

        return true
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Timber.i("[Console] : ${consoleMessage?.messageLevel()} : ${consoleMessage?.lineNumber()} : ${consoleMessage?.message()}")

        return super.onConsoleMessage(consoleMessage)
    }
}