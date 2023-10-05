package com.hproject.banttang.data.user.model.mapper

import com.hproject.banttang.data.user.model.LoginData
import com.hproject.banttang.domain.user.entity.Login

fun LoginData.toDomainEntity(): Login {
    return Login(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}