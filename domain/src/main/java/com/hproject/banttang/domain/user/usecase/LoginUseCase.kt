package com.hproject.banttang.domain.user.usecase

import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import javax.inject.Inject

/**
 * 유저 UseCase
 *
 * @author hjkim
 * @since 2023/02/16
 **/
class LoginUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    /**
     * 로그인
     *
     * @see IUserRepository.login
     * @author hjkim
     * @since 2023/02/16
     **/
    suspend operator fun invoke(
        requestParam: LoginRequestParam
    ) = repository.login(
        requestParam = requestParam
    )
}