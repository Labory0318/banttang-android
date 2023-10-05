package com.hproject.core.data.data_source.remote.retrofit.interceptor

import com.google.gson.JsonParser
import com.hproject.core.data.data_source.remote.retrofit.interceptor.NetworkPrettyLogger.Companion.DEFAULT_NETWORK_LOG_TAG
import com.hproject.core.extension.isJson
import com.hproject.core.utils.GsonUtil
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * OkhttpClient Interceptor for Network Log
 *
 * @param networkLogTag 네트워크 Log TAG, Default: [DEFAULT_NETWORK_LOG_TAG]
 * @author thomas
 * @since 2022/11/24
 **/
class NetworkPrettyLogger(
    private val networkLogTag: String = DEFAULT_NETWORK_LOG_TAG
) : HttpLoggingInterceptor.Logger {
    companion object {
        private const val DEFAULT_NETWORK_LOG_TAG = "Network"
    }

    override fun log(message: String) {
        try {
            if (message.isJson()) {
                Timber.tag(networkLogTag).d(" \n--> START RESPONSE\n${GsonUtil.toJson(JsonParser.parseString(message))}\n<-- END RESPONSE")
            }
            else {
                Timber.tag(networkLogTag).d(message)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}