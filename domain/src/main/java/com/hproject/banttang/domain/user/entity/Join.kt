package com.hproject.banttang.domain.user.entity

import com.hproject.banttang.domain.user.define.LoginProviderDefine

data class Join(
    val id: Long,
    var nickname: String?,
    val provider: LoginProviderDefine,
    val userKey: String,
    var imageUrl: String?
)
