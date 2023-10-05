package com.hproject.banttang.domain.user.usecase

import com.hproject.banttang.domain.user.repository.IUserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    suspend operator fun invoke() = repository.getUserInfo()
}