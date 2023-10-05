package com.hproject.banttang.domain.user.request_param

import com.hproject.banttang.domain.user.define.LoginProviderDefine

/**
 * 자동로그인 요청 Param
 *
 * @author hjkim
 * @since 2023/03/23
**/
data class ReIssueRequestParam(
    val accessToken: String,
    val refreshToken: String,
    val fcmToken: String
)