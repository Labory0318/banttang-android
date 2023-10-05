package com.hproject.banttang.domain.version.request_param

import com.hproject.banttang.domain.version.define.ApplicationTypeDefine

/**
 * 버전 체크 요청 Param
 *
 * @author hjkim
 * @since 2023/02/15
 **/
data class VersionCheckRequestParam(
    // 버전 체크할 Application 타입 AOS 고정
    val applicationType: ApplicationTypeDefine = ApplicationTypeDefine.AOS,

    // 빌드된 앱 버전
    val version: String
)