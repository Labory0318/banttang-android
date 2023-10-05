package com.hproject.banttang.domain.user.request_param

import com.hproject.banttang.domain.user.define.LoginProviderDefine

/**
 * 로그인 요청 Param
 *
 * @author hjkim
 * @since 2023/02/16
 **/
data class LoginRequestParam(
    val provider: LoginProviderDefine,
    val providerAccessToken: String,
    val fcmToken: String
)