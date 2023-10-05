package com.hproject.banttang.data.user.model.mapper

import com.hproject.banttang.data.user.model.UserData
import com.hproject.banttang.domain.user.entity.Join

fun UserData.toDomainEntity() : Join =
    Join(
        id =id,
        nickname = nickname,
        provider = provider,
        userKey = userKey,
        imageUrl = imageUrl
    )