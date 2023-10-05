package com.hproject.banttang.data.version.repository

import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.version.data_source.remote.IVersionCheckRemoteDataSource
import com.hproject.banttang.data.version.model.mapper.toDomainEntity
import com.hproject.banttang.domain.user.entity.UserInfoResponse
import com.hproject.banttang.domain.version.entity.VersionCheck
import com.hproject.banttang.domain.version.entity.VersionCheckResponse
import com.hproject.banttang.domain.version.repository.IVersionCheckRepository
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam
import com.hproject.core.extension.toEnumOrNull
import javax.inject.Inject

class VersionCheckRemoteRepository @Inject constructor(
    private val remoteDataSource: IVersionCheckRemoteDataSource
): IVersionCheckRepository {

    /**
     * 버전 정보 조회
     *
     * @param requestParam  요청 정보
     * @see VersionCheckRequestParam
     * @author hjkim
     * @since 2023/02/15
     **/
    override suspend fun versionCheck(
        requestParam: VersionCheckRequestParam
    ): Result<VersionCheckResponse<VersionCheck>> {
        return remoteDataSource.versionCheck(
            requestParam = requestParam
        ).map {
            when (it?.meta?.result) {
                BanttangBaseResponse.Meta.MetaResult.OK -> {
                    VersionCheckResponse(
                        result = VersionCheckResponse.Result.GET_VERSION_INFO_SUCCESS,
                        data = it.content?.toDomainEntity()
                    )
                }
                else -> {
                    VersionCheckResponse(
                        result = it?.meta?.errorCode?.replace("-", "_")?.uppercase().toEnumOrNull<VersionCheckResponse.Result>()
                    )
                }
            }
        }
    }
}