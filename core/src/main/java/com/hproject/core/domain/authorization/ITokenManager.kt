package com.hproject.core.domain.authorization

interface ITokenManager {
    /**
     * Header Key 값
     *
     * @author thomas
     * @since 2022/11/28
     **/
    fun getHeaderName(): String

    /**
     * Header TID 형식
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    fun getHeaderTid(): String

    /**
     * Header DEVICE 형식
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    fun getHeaderDevice(): String

    /**
     * Header MODEL 형식
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    fun getHeaderModel(): String

    /**
     * Header App Version 형식
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    fun getHeaderAppVersion(): String

    /**
     * Header Value 형식
     * - Ex) Bearer %s
     *
     * @author thomas
     * @since 2022/11/28
     **/
    fun getHeaderValueFormat(): String

    /**
     * 저장 되어 있는 Access 토큰 정보
     *
     * @author thomas
     * @since 2022/11/28
     **/
    fun getAccessToken(): String?

    /**
     * Access 토큰 정보 저장
     *
     * @param token    토큰 정보
     * @author thomas
     * @since 2022/11/28
     **/
    fun setAccessToken(token: String?)

    /**
     * 저장 되어 있는 Refresh 토큰 정보
     *
     * @author hjkim
     * @since 2023/02/17
     **/
    fun getRefreshToken(): String?

    /**
     * Refresh 토큰 정보 저장
     *
     * @param token    토큰 정보
     * @author hjkim
     * @since 2023/02/17
     **/
    fun setRefreshToken(token: String?)

    /**
     * 저장 되어 있는 Fcm 토큰 정보
     *
     * @author thomas
     * @since 2023/02/16
     **/
    fun getFcmToken(): String?

    /**
     * Fcm 토큰 정보 저장
     *
     * @param token    토큰 정보
     * @author hjkim
     * @since 2023/02/16
     **/
    fun setFcmToken(token: String)

    /**
     * 저장 되어 있는 토큰 만료 처리
     *
     * @author thomas
     * @since 2022/11/28
     **/
    fun clearToken()

    /**
     * 토큰 유효성 검사
     *
     * @author hjkim
     * @since 2023/02/17
     **/
    fun isValidToken(): Boolean
}