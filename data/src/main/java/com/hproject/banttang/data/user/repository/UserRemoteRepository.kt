package com.hproject.banttang.data.user.repository

import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.user.data_source.local.IUserLocalDataSource
import com.hproject.banttang.data.user.data_source.remote.IUserRemoteDataSource
import com.hproject.banttang.data.user.model.mapper.toDomainEntity
import com.hproject.banttang.domain.user.entity.*
import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam
import com.hproject.core.extension.toEnumOrNull
import javax.inject.Inject

class UserRemoteRepository @Inject constructor(
    private val remoteDataSource: IUserRemoteDataSource,
    private val localDataSource : IUserLocalDataSource
): IUserRepository {

    /**
     * 로그인
     *
     * @param requestParam  요청 정보
     * @see LoginRequestParam
     * @author hjkim
     * @since 2023/02/16
     **/
    override suspend fun login(
        requestParam: LoginRequestParam
    ): Result<LoginResponse<Login>> {
        return remoteDataSource.login(
            requestParam = requestParam
        ).map {
            when (it?.meta?.result) {
                BanttangBaseResponse.Meta.MetaResult.OK -> {
                    LoginResponse(
                        result = LoginResponse.Result.LOGIN_SUCCESS,
                        data = it.content?.toDomainEntity()
                    )
                }
                else -> {
                    LoginResponse(
                        result = it?.meta?.errorCode?.replace("-", "_")?.uppercase().toEnumOrNull<LoginResponse.Result>()
                    )
                }
            }
        }
    }

    override suspend fun getUserInfo(): Result<UserInfoResponse<UserInfo>> =
        remoteDataSource.getUserInfo().map {
            if(it?.meta?.result == BanttangBaseResponse.Meta.MetaResult.OK){
                it.content?.let { userInfo -> localDataSource.saveUserInfo(userInfo) }
                UserInfoResponse(
                    result = UserInfoResponse.Result.GET_USER_INFO_SUCCESS,
                    data = it.content?.toDomainEntity()
                )
            }else{
                UserInfoResponse(
                    result = it?.meta?.errorCode?.replace("-", "_")?.uppercase().toEnumOrNull<UserInfoResponse.Result>()
                )
            }
        }

    override suspend fun join(requestParam: JoinRequestParam): Result<JoinResponse<Join>>  =
        remoteDataSource.join(requestParam = requestParam).map {
            if(it?.meta?.result == BanttangBaseResponse.Meta.MetaResult.OK){
                JoinResponse(
                    result = JoinResponse.Result.JOIN_SUCCESS,
                    data = it.content?.toDomainEntity()
                )
            }else{
                JoinResponse(
                    result = it?.meta?.errorCode?.replace("-", "_")?.uppercase().toEnumOrNull<JoinResponse.Result>()
                )
            }
        }

    override suspend fun updateProfile(requestParam: UpdateProfileRequestParam): Result<UpdateProfileResponse<Nothing>> =
        remoteDataSource.updateProfile(requestParam = requestParam).map {
            UpdateProfileResponse(
                result = UpdateProfileResponse.Result.OK,
                data = it?.content
            )
        }

    /**
     * 자동로그인
     *
     * @param requestParam  요청 정보
     * @see ReIssueRequestParam
     * @author hjkim
     * @since 2023/03/23
     **/
    override suspend fun reIssue(
        requestParam: ReIssueRequestParam
    ): Result<LoginResponse<Login>> {
        return remoteDataSource.reIssue(
            requestParam = requestParam
        ).map {
            when (it?.meta?.result) {
                BanttangBaseResponse.Meta.MetaResult.OK -> {
                    LoginResponse(
                        result = LoginResponse.Result.LOGIN_SUCCESS,
                        data = it.content?.toDomainEntity()
                    )
                }
                else -> {
                    LoginResponse(
                        result = it?.meta?.errorCode?.replace("-", "_")?.uppercase().toEnumOrNull<LoginResponse.Result>()
                    )
                }
            }
        }
    }
}