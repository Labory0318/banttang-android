package com.hproject.core.presentation.web_view.client

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.webkit.*
import com.hproject.core.R
import timber.log.Timber
import java.net.URISyntaxException

class CoreWebViewClient(
    private val context: Context
) : WebViewClient() {
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Timber.i("url=${request?.url}")

        val url: String? = request?.url?.toString()

        if(!url.isNullOrBlank()) {
            when {
                url.startsWith("http://") || url.startsWith("https://") || url.startsWith("javascript:") -> {
                    view?.loadUrl(url)
                }
                else -> {
                    var intent: Intent? = null
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    }
                    catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }

                    val uri = Uri.parse(intent?.dataString)
                    intent = Intent(Intent.ACTION_VIEW, uri)

                    try {
                        view?.context?.startActivity(intent)
                    }
                    catch (ane: ActivityNotFoundException) {
                        when {
                            url.startsWith(ISP_MOBILE_URL_PREFIX) -> {
                                view?.context?.let { context ->
                                    AlertDialog.Builder(context)
                                        .setMessage(R.string.msg_isp_not_exist)
                                        .setPositiveButton(R.string.ok) { _, _ ->
                                            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse(ISP_MOBILE_MARKET_URL)
                                            })
                                        }
                                        .setNegativeButton(R.string.cancel) { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .create()
                                        .show()

                                }
                            }
                            url.startsWith("intent://") -> {
                                try {
                                    view?.context?.let { context ->
                                        val exceptionIntent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(MARKET_URL_SEARCH_FORMAT.format(`package`))
                                        }
                                        context.startActivity(exceptionIntent)
                                    }
                                }
                                catch (e: URISyntaxException) {
                                    e.printStackTrace()
                                }
                            }
                        }

                    }
                }
            }
        }

        //        if (!url.isNullOrBlank()) {
//            val intent = parseIntent(uri = url)
//
//            when {
//                isIntent(url = url) -> {
//                    if (isExistActivity(intent = intent) or isExistPackage(intent = intent)) {
//                        intent?.let { startActivity(intent = it) }
//                    }
//                }
//                isMarket(url = url) -> {
//                    intent?.let { startActivity(it) }
//                }
//            }
//        }


        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)

        Timber.e("[Error] errorCode=${error?.errorCode}}, errorDescription=${error?.description}")
    }

    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        super.onReceivedHttpError(view, request, errorResponse)

        Timber.e("[HttpError] statusCode=${errorResponse?.statusCode}")
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)

        Timber.e("[SslError] url=${error?.url}")
    }

    private fun isIntent(url: String) = url.matches(Regex("^intent:?\\w*://\\S+$"))

    private fun isMarket(url: String) = url.matches(Regex("^market://\\S+$"))

    private fun isExistActivity(intent: Intent?): Boolean {
        try {
            val packageName = intent?.`package`
            if (packageName.isNullOrEmpty()) {
                return false
            }
            return context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }
    }

    private fun isExistPackage(intent: Intent?): Boolean = intent?.let {
        context.packageManager.getLaunchIntentForPackage(it.`package`.toString()) != null
    } ?: false

    private fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    private fun goToMarket(intent: Intent) {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=${intent.`package`}")
        })
    }

    /**
     * parse uri to intent
     *
     * @author thomas
     * @since 2023/01/02
     **/
    private fun parseIntent(uri: String): Intent? {
        return try {
            Intent.parseUri(uri, Intent.URI_INTENT_SCHEME)
        }
        catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val MARKET_URL_SEARCH_FORMAT = "market://search?q=%s"

        private const val ISP_MOBILE_URL_PREFIX = "ispmobile://"
        private const val ISP_MOBILE_MARKET_URL = "market://details?id=kvp.jjy.MispAndroid320"
    }
}