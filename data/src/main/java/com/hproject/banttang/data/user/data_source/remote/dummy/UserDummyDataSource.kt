package com.hproject.banttang.data.user.data_source.remote.dummy

import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.user.data_source.remote.IUserRemoteDataSource
import com.hproject.banttang.data.user.model.LoginData
import com.hproject.banttang.data.user.model.UserData
import com.hproject.banttang.data.user.model.UserInfoData
import com.hproject.banttang.domain.user.define.LoginProviderDefine
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam

class UserDummyDataSource : IUserRemoteDataSource {

    override suspend fun login(
        requestParam: LoginRequestParam
    ): Result<BanttangSingleResponse<LoginData>?> {
        val dummyLoginData = BanttangSingleResponse(
            content = LoginData (
                grantType = "Bearer",
                accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2NzY2ODcyMTJ9.RIgtOxq2JUIGIjNMrAM9z4BuaLlh_zbWM-RT13ji8HRjGrCKQDVi_96Si82VggOH_T2987JgAxItX0SW06h-AA",
                accessTokenExpiredIn = 1676687212182,
                refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NzkxOTI4MTJ9.jppdPN0PyFqwYxEPKjgtDzJa3nfqV5dQ1oAd16eHXZCuyROfskfc0sgYcXJLfmhHe_BEpeiPhIyihtxqIW7x7w"
            )
        )
        dummyLoginData.meta?.result = BanttangBaseResponse.Meta.MetaResult.OK

        return Result.success(
            value = dummyLoginData
        )
    }

    override suspend fun getUserInfo(): Result<BanttangSingleResponse<UserInfoData>?> {
        val dummyUserInfoData = BanttangSingleResponse(content = UserInfoData(id = 0, nickName = "song", imageUrl = ""))
        dummyUserInfoData.meta?.result = BanttangBaseResponse.Meta.MetaResult.OK

        return Result.success(
            value = dummyUserInfoData
        )
    }

    override suspend fun join(requestParam: JoinRequestParam): Result<BanttangSingleResponse<UserData>?> {
        val dummyJoinData = BanttangSingleResponse(content = UserData(
            id = 0,
            nickname = "song",
            provider = LoginProviderDefine.KAKAO,
            userKey = "userKye",
            imageUrl = "image"
        ))
        dummyJoinData.meta?.result = BanttangBaseResponse.Meta.MetaResult.OK

        return Result.success(
            value = dummyJoinData
        )
    }

    override suspend fun updateProfile(requestParam: UpdateProfileRequestParam): Result<BanttangSingleResponse<Nothing>?> =
        Result.success(
            BanttangSingleResponse(content = null)
        )

    override suspend fun reIssue(requestParam: ReIssueRequestParam): Result<BanttangSingleResponse<LoginData>?> {
        val dummyLoginData = BanttangSingleResponse(
            content = LoginData (
                grantType = "Bearer",
                accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2NzY2ODcyMTJ9.RIgtOxq2JUIGIjNMrAM9z4BuaLlh_zbWM-RT13ji8HRjGrCKQDVi_96Si82VggOH_T2987JgAxItX0SW06h-AA",
                accessTokenExpiredIn = 1676687212182,
                refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NzkxOTI4MTJ9.jppdPN0PyFqwYxEPKjgtDzJa3nfqV5dQ1oAd16eHXZCuyROfskfc0sgYcXJLfmhHe_BEpeiPhIyihtxqIW7x7w"
            )
        )
        dummyLoginData.meta?.result = BanttangBaseResponse.Meta.MetaResult.OK

        return Result.success(
            value = dummyLoginData
        )
    }

}