package com.hproject.banttang.data.version.model.mapper

import com.hproject.banttang.data.version.model.VersionCheckData
import com.hproject.banttang.domain.version.entity.VersionCheck

fun VersionCheckData.toDomainEntity(): VersionCheck {
    return VersionCheck(
        applicationCheckResultCode = applicationCheckResultCode
    )
}