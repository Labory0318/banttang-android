package com.hproject.banttang.domain.common.entity

open class BaseResponse<R: Enum<*>, T>(
    val result: R,
    val data: T? = null
)