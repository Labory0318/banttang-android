package com.hproject.banttang.domain.user.repository

import com.hproject.banttang.domain.user.entity.*
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam

interface IUserRepository {

    /**
     * 로그인
     * @param requestParam  요청 정보
     * @see LoginRequestParam
     * @author hjkim
     * @since 2023/02/16
     **/
    suspend fun login(
        requestParam: LoginRequestParam
    ): Result<LoginResponse<Login>>

    suspend fun getUserInfo() : Result<UserInfoResponse<UserInfo>>

    suspend fun join(requestParam: JoinRequestParam) : Result<JoinResponse<Join>>

    suspend fun updateProfile(requestParam: UpdateProfileRequestParam) : Result<UpdateProfileResponse<Nothing>>

    /**
     * 자동 로그인
     *
     * @param requestParam  요청 정보
     * @see ReIssueRequestParam
     * @author hjkim
     * @since 2023/03/23
     **/
    suspend fun reIssue(
        requestParam: ReIssueRequestParam
    ): Result<LoginResponse<Login>>
}