package com.hproject.banttang.domain.user.usecase

import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import javax.inject.Inject


class UpdateProfileUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    suspend operator fun invoke(
        requestParam: UpdateProfileRequestParam
    ) = repository.updateProfile(
        requestParam = requestParam
    )
}