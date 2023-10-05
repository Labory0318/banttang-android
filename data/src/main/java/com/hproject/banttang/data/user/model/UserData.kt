package com.hproject.banttang.data.user.model

import com.hproject.banttang.domain.user.define.LoginProviderDefine

data class UserData(
    val id: Long,
    var nickname: String?,
    val provider: LoginProviderDefine,
    val userKey: String,
    var imageUrl: String?
)
