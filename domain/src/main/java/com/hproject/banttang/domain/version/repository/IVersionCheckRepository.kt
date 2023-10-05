package com.hproject.banttang.domain.version.repository

import com.hproject.banttang.domain.version.entity.VersionCheck
import com.hproject.banttang.domain.version.entity.VersionCheckResponse
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam

interface IVersionCheckRepository {

    /**
     * 버전 정보 조회
     *
     * @param requestParam  요청 정보
     * @see VersionCheckRequestParam
     * @author hjkim
     * @since 2023/02/15
     **/
    suspend fun versionCheck(
        requestParam: VersionCheckRequestParam
    ): Result<VersionCheckResponse<VersionCheck>>
}