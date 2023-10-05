package com.hproject.banttang.data.user.data_source.local

import com.hproject.banttang.data.user.model.UserInfoData

interface IUserLocalDataSource {

    fun saveUserInfo(userInfoData: UserInfoData)
}