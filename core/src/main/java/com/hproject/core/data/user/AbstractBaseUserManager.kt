package com.hproject.core.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class AbstractBaseUserManager<UserInfo> {

    private val _user: MutableLiveData<UserInfo?> = MutableLiveData()
    val user: LiveData<UserInfo?> get() = _user

    fun saveUserInfo(userInfo: UserInfo){
        _user.postValue(userInfo)
    }

    fun getUserInfo() : UserInfo? = user.value
}