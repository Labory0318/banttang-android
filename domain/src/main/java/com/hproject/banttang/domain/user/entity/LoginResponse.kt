package com.hproject.banttang.domain.user.entity

import com.hproject.banttang.domain.common.entity.BaseResponse

/**
 * @author hjkim
 * @since 2023/02/16
 */
class LoginResponse<T>(
    result: Result? = Result.FAIL,
    data: T? = null
) : BaseResponse<LoginResponse.Result, T>(
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
        LOGIN_SUCCESS,
        AUTHENTICATION_FAIL,
        USER_NOT_FOUND,
        FAIL
    }
}