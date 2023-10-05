package com.hproject.banttang.domain.user.entity

import com.hproject.banttang.domain.common.entity.BaseResponse

class UserInfoResponse<T>(
    result: Result? = Result.FAIL,
    data: T? = null
) : BaseResponse<UserInfoResponse.Result, T>(
    result = result!!,
    data = data
) {
    enum class Result {
        GET_USER_INFO_SUCCESS,
        FAIL
    }
}