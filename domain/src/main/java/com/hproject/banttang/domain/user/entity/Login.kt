package com.hproject.banttang.domain.user.entity

/**
 * @author hjkim
 * @since 2023/02/16
 */
data class Login(
    val accessToken: String?,
    val refreshToken: String?
)