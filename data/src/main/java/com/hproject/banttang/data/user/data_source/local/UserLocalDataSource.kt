package com.hproject.banttang.data.user.data_source.local

import com.hproject.banttang.data.user.manager.UserManager
import com.hproject.banttang.data.user.model.UserInfoData
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userInfoManager: UserManager
) : IUserLocalDataSource{

    override fun saveUserInfo(userInfoData: UserInfoData) {
        userInfoManager.saveUserInfo(userInfo = userInfoData)
    }


}