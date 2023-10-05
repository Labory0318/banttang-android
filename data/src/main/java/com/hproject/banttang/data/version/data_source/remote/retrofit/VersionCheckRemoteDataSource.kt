package com.hproject.banttang.data.version.data_source.remote.retrofit

import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.version.data_source.remote.IVersionCheckRemoteDataSource
import com.hproject.banttang.data.version.model.VersionCheckData
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam
import com.hproject.core.data.data_source.remote.retrofit.AbstractBaseRetrofitRemoteDataSource
import javax.inject.Inject

class VersionCheckRemoteDataSource @Inject constructor(
    private val versionCheckApi: IVersionCheckRetrofitApi
) : AbstractBaseRetrofitRemoteDataSource(),
    IVersionCheckRemoteDataSource {

    /**
     * 버전 정보 조회
     *
     * @author hjkim
     * @since 2023/02/15
     **/
    override suspend fun versionCheck(
        requestParam: VersionCheckRequestParam
    ): Result<BanttangSingleResponse<VersionCheckData>?> {
        return runWithHandlingResult { versionCheckApi.checkVersion(requestParam) }
    }
}