package com.hproject.banttang.data.user.model

import com.google.gson.annotations.SerializedName

data class UserInfoData(
    val id : Int?,
    @SerializedName("nickname")
    val nickName : String?,
    val imageUrl : String?
)
