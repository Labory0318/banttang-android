package com.hproject.banttang.data.network.model

import com.google.gson.annotations.SerializedName

open class BanttangBaseResponse {
    val meta: Meta? = null

    data class Meta(
        var result: MetaResult,
        val errorCode: String?,
        val errorMessage: String?
    ) {
        enum class MetaResult {
            @SerializedName("ok")
            OK,
            @SerializedName("fail")
            FAIL
        }
    }
}
