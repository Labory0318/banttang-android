package com.hproject.banttang.data.version.data_source.remote

import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.version.model.VersionCheckData
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam

interface IVersionCheckRemoteDataSource {

    /**
     * 버전 정보 조회
     *
     * @author hjkim
     * @since 2023/02/15
     **/
    suspend fun versionCheck(
        requestParam: VersionCheckRequestParam
    ): Result<BanttangSingleResponse<VersionCheckData>?>
}