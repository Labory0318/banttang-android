package com.hproject.banttang.domain.user.usecase

import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import javax.inject.Inject


class JoinUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    suspend operator fun invoke(
        requestParam: JoinRequestParam
    ) = repository.join(
        requestParam = requestParam
    )
}