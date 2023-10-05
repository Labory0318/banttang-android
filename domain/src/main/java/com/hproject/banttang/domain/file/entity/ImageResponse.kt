package com.hproject.banttang.domain.file.entity

import com.hproject.banttang.domain.common.entity.BaseResponse

class ImageResponse<T>(
    result: Result? = Result.FAIL,
    data: T? = null
) : BaseResponse<ImageResponse.Result, T>(
    result = result!!,
    data = data
) {
    enum class Result {
        GET_IMAGE_SUCCESS,
        FAIL
    }
}