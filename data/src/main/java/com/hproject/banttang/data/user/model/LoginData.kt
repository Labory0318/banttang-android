package com.hproject.banttang.data.user.model

/**
 * @author hjkim
 * @since 2023/02/16
 */
data class LoginData(
    val grantType: String?,
    val accessToken: String?,
    val accessTokenExpiredIn: Long?,
    val refreshToken: String?
)