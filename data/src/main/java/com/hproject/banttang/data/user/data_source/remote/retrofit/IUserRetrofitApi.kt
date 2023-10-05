package com.hproject.banttang.data.user.data_source.remote.retrofit

import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.user.model.*
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * 유저 관련 API
 *
 * @author hjkim
 * @since 2023/02/16
 **/
interface IUserRetrofitApi {
    companion object {
        const val USER_API = "/api/user"
        // 로그인
        private const val LOGIN = "$USER_API/login"
        // 회원가입
        private const val JOIN = "$USER_API/join"
        // 자동로그인
        private const val REISSUE = "$USER_API/reissue"
    }

    /**
     * 로그인
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    @POST(LOGIN)
    suspend fun login(
        @Body body: LoginRequestParam
    ): Response<BanttangSingleResponse<LoginData>>

    @GET(USER_API)
    suspend fun getUserInfo() : Response<BanttangSingleResponse<UserInfoData>>

    @POST(JOIN)
    suspend fun join(@Body body: JoinRequestParam): Response<BanttangSingleResponse<UserData>>

    @PUT(USER_API)
    suspend fun updateProfile(@Body body: UpdateProfileRequestParam): Response<BanttangSingleResponse<Nothing>>
    
    /**
     * 자동로그인
     *
     * @author hjkim
     * @since 2023/03/23
    **/
    @POST(REISSUE)
    suspend fun reIssue(
        @Body body: ReIssueRequestParam
    ): Response<BanttangSingleResponse<LoginData>>

}