package com.hproject.banttang.domain.version.entity

import com.hproject.banttang.domain.common.entity.BaseResponse

class VersionCheckResponse<T>(
    result: Result? = Result.FAIL,
    data: T? = null
) : BaseResponse<VersionCheckResponse.Result, T>(
    result = result!!,
    data = data
) {
    /**
     * Error Exception Case
     *
     * @author hjkim
     * @since 2023/02/15
     **/
    enum class Result {
        GET_VERSION_INFO_SUCCESS,
        FAIL
    }
}