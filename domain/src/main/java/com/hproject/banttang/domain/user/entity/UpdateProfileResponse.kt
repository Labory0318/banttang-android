package com.hproject.banttang.domain.user.entity

import com.hproject.banttang.domain.common.entity.BaseResponse

class UpdateProfileResponse<T>(
    result: Result = Result.OK,
    data: T? = null
) : BaseResponse<UpdateProfileResponse.Result, T>(
    result = result,
    data = data
) {
    enum class Result {
        OK
    }
}