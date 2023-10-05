package com.hproject.banttang.data.version.model

import com.hproject.banttang.domain.version.define.ApplicationTypeDefine
import com.hproject.banttang.domain.version.define.VersionStatusDefine

/**
 * @author hjkim
 * @since 2023/02/15
 */
data class VersionCheckData(
    val applicationType: ApplicationTypeDefine,
    val version: String,
    val applicationCheckResultCode: VersionStatusDefine
)
