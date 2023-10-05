package com.hproject.core.data.authorization

import com.hproject.core.domain.authorization.ITokenManager

/**
 * 토큰 정보 관리 매니저
 *
 * @author thomas
 * @since 2022/11/28
 **/
class TokenManager private constructor() : ITokenManager {
    // 저장된 Access 토큰 정보
    private var accessToken: String? = null
    // 저장된 Refresh 토큰 정보
    private var refreshToken: String? = null
    // 저장된 Fcm 토큰 정보
    private var fcmToken: String? = null

    /**
     * Header Name
     *
     * @author thomas
     * @since 2022/11/28
     **/
    override fun getHeaderName(): String = HEADER_NAME

    /**
     * Header Value Format
     *
     * @author thomas
     * @since 2022/11/28
     **/
    override fun getHeaderValueFormat(): String = HEADER_VALUE_FORMAT

    /**
     * Header TID
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    override fun getHeaderTid(): String = HEADER_TID

    /**
     * Header Device
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    override fun getHeaderDevice(): String = HEADER_DEVICE

    /**
     * Header Model
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    override fun getHeaderModel(): String = HEADER_MODEL

    /**
     * Header App Version
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    override fun getHeaderAppVersion(): String = HEADER_APP_VERSION

    /**
     * 저장된 Access 토큰 정보
     *
     * @author thomas
     * @since 2022/11/28
     **/
    override fun getAccessToken(): String? = accessToken

    /**
     * 토큰 Access 정보 저장
     *
     * @author thomas
     * @since 2022/11/28
     **/
    override fun setAccessToken(token: String?) {
        accessToken = token
    }

    /**
     * 저장 되어 있는 Refresh 토큰 정보
     *
     * @author hjkim
     * @since 2023/02/17
     **/
    override fun getRefreshToken(): String? = refreshToken

    /**
     * Refresh 토큰 정보 저장
     *
     * @param token    토큰 정보
     * @author hjkim
     * @since 2023/02/17
     **/
    override fun setRefreshToken(token: String?) {
        refreshToken = token
    }

    /**
     * 저장된 Fcm 토큰 정보
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    override fun getFcmToken(): String? = fcmToken

    /**
     * 토큰 Fcm 정보 저장
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    override fun setFcmToken(token: String) {
        fcmToken = token
    }

    /**
     * 저장된 토큰 정보 페기
     *
     * @author thomas
     * @since 2022/11/28
     **/
    override fun clearToken() {
        accessToken = null
    }

    /**
     * 토큰 유효성 검사
     *
     * @author hjkim
     * @since 2023/02/17
     **/
    override fun isValidToken(): Boolean {
        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }

    companion object {
        @JvmStatic
        val instance by lazy { TokenManager() }

        private const val HEADER_TID = "H-TID"
        private const val HEADER_DEVICE = "H-DEVICE"
        private const val HEADER_MODEL = "H-MODEL"
        private const val HEADER_APP_VERSION = "H-APP-VERSION"

        private const val HEADER_NAME = "Authorization"
        private const val HEADER_VALUE_FORMAT = "Bearer %s"
    }
}