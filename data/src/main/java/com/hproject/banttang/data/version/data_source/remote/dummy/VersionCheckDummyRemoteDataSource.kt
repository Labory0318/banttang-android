package com.hproject.banttang.data.version.data_source.remote.dummy

import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.version.data_source.remote.IVersionCheckRemoteDataSource
import com.hproject.banttang.data.version.model.VersionCheckData
import com.hproject.banttang.domain.version.define.ApplicationTypeDefine
import com.hproject.banttang.domain.version.define.VersionStatusDefine
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam

class VersionCheckDummyRemoteDataSource : IVersionCheckRemoteDataSource {
    override suspend fun versionCheck(requestParam: VersionCheckRequestParam): Result<BanttangSingleResponse<VersionCheckData>?> {
        val dummyVersionCheckData = BanttangSingleResponse(
            content = VersionCheckData (
                applicationType = ApplicationTypeDefine.AOS,
                version = "1.1.0",
                applicationCheckResultCode = VersionStatusDefine.LATEST
            )
        )
        dummyVersionCheckData.meta?.result = BanttangBaseResponse.Meta.MetaResult.OK

        return Result.success(
            value = dummyVersionCheckData
        )
    }
}