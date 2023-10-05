package com.hproject.core.data.data_source.remote.retrofit.interceptor

import android.os.Build
import com.hproject.core.domain.authorization.ITokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * AuthorizationTokenInterceptor
 *
 * @author thomas
 * @since 2022/11/28
 * @see ITokenManager
 **/
class AuthorizationTokenInterceptor @Inject constructor(
    private val tokenManager: ITokenManager
) : Interceptor {
    /**
     * authorization header 추가
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestWithTokenBuilder = originRequest.newBuilder()
            .addHeader(tokenManager.getHeaderTid(), (System.currentTimeMillis() / 1000L).toString())
            .addHeader(tokenManager.getHeaderDevice(), "Android")
            .addHeader(tokenManager.getHeaderModel(), "${Build.BRAND} ${Build.MODEL}")
            .addHeader(tokenManager.getHeaderAppVersion(),Build.VERSION.RELEASE)

        tokenManager.getAccessToken()?.let { token ->
            requestWithTokenBuilder.addHeader(
                tokenManager.getHeaderName(),
                tokenManager.getHeaderValueFormat().format(token)
            )
        }

        return chain.proceed(request = requestWithTokenBuilder.build())
    }
}