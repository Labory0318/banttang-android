package com.hproject.banttang.data.version.data_source.remote.retrofit

import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.version.model.VersionCheckData
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 앱 버전 관련 API
 *
 * @author hjkim
 * @since 2023/02/15
 **/
interface IVersionCheckRetrofitApi {
    companion object {
        const val VERSION_API = "/api/version"
    }

    /**
     * 버전 체크
     *
     * @param body      App Version 정보
     * @author hjkim
     * @since 2023/02/15
     **/
    @POST(VERSION_API)
    suspend fun checkVersion(
        @Body body: VersionCheckRequestParam
    ): Response<BanttangSingleResponse<VersionCheckData>?>
}