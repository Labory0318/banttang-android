package com.hproject.banttang.data.user.data_source.remote.retrofit

import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.user.data_source.remote.IUserRemoteDataSource
import com.hproject.banttang.data.user.model.LoginData
import com.hproject.banttang.data.user.model.UserData
import com.hproject.banttang.data.user.model.UserInfoData
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam
import com.hproject.core.data.data_source.remote.retrofit.AbstractBaseRetrofitRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: IUserRetrofitApi
) : AbstractBaseRetrofitRemoteDataSource(),
    IUserRemoteDataSource {

    /**
     * 로그인
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    override suspend fun login(
        requestParam: LoginRequestParam
    ): Result<BanttangSingleResponse<LoginData>?> {
        return runWithHandlingResult { userApi.login(requestParam) }
    }

    override suspend fun getUserInfo(): Result<BanttangSingleResponse<UserInfoData>?> =
        runWithHandlingResult { userApi.getUserInfo() }

    override suspend fun join(requestParam: JoinRequestParam): Result<BanttangSingleResponse<UserData>?> =
        runWithHandlingResult { userApi.join(requestParam) }

    override suspend fun updateProfile(requestParam: UpdateProfileRequestParam): Result<BanttangSingleResponse<Nothing>?> =
        runWithHandlingResult { userApi.updateProfile(requestParam) }

    /**
     * 자동로그인
     *
     * @author hjkim
     * @since 2023/03/23
     **/
    override suspend fun reIssue(
        requestParam: ReIssueRequestParam
    ): Result<BanttangSingleResponse<LoginData>?> {
        return runWithHandlingResult { userApi.reIssue(requestParam) }
    }
}