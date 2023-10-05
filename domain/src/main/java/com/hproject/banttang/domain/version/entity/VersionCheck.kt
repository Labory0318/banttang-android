package com.hproject.banttang.domain.version.entity

import com.hproject.banttang.domain.version.define.VersionStatusDefine

data class VersionCheck(
    // 앱 버전 상태
    val applicationCheckResultCode: VersionStatusDefine
)
