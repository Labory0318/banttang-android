package com.hproject.banttang.data.network.model

/**
 * @author hjkim
 * @since 2023/02/15
 */
data class BanttangSingleResponse<T>(
    val content: T?
): BanttangBaseResponse()