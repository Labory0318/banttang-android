package com.hproject.banttang.domain.user.request_param

import com.hproject.banttang.domain.user.define.LoginProviderDefine
import java.io.Serializable

data class JoinRequestParam(
    val provider: LoginProviderDefine,
    val providerAccessToken: String,
    val userKey: String,
    var name: String? = null,
    var imageUrl: String? = null
): Serializable