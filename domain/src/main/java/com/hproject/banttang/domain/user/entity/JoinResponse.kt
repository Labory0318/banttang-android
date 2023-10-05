package com.hproject.banttang.domain.user.entity

import com.hproject.banttang.domain.common.entity.BaseResponse

class JoinResponse<T>(
    result: Result? = Result.FAIL,
    data: T? = null
) : BaseResponse<JoinResponse.Result, T>(
    result = result!!,
    data = data
) {
    enum class Result {
        JOIN_SUCCESS,
        DUPLICATED_JOINING,
        FAIL
    }
}