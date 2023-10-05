package com.hproject.banttang.data.user.data_source.remote

import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.user.model.LoginData
import com.hproject.banttang.data.user.model.UserData
import com.hproject.banttang.data.user.model.UserInfoData
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam

interface IUserRemoteDataSource {

    /**
     * 로그인
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    suspend fun login(
        requestParam: LoginRequestParam
    ): Result<BanttangSingleResponse<LoginData>?>

    suspend fun getUserInfo(): Result<BanttangSingleResponse<UserInfoData>?>

    suspend fun join(requestParam: JoinRequestParam): Result<BanttangSingleResponse<UserData>?>

    suspend fun updateProfile(requestParam : UpdateProfileRequestParam): Result<BanttangSingleResponse<Nothing>?>

    /**
     * 자동로그인
     *
     * @author hjkim
     * @since 2023/03/23
     **/
    suspend fun reIssue(
        requestParam: ReIssueRequestParam
    ): Result<BanttangSingleResponse<LoginData>?>
}