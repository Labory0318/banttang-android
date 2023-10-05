package com.hproject.banttang.data.user.model.mapper

import com.hproject.banttang.data.user.model.UserInfoData
import com.hproject.banttang.domain.user.entity.UserInfo

fun UserInfoData.toDomainEntity() : UserInfo =
    UserInfo(
        id = id,
        nickName = nickName,
        imageUrl = imageUrl
    )