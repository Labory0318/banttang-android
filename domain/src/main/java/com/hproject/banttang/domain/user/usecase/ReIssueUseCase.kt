package com.hproject.banttang.domain.user.usecase

import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam
import javax.inject.Inject

/**
 * 자동로그인 UseCase
 *
 * @author hjkim
 * @since 2023/03/23
 **/
class ReIssueUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    /**
     * 자동로그인
     *
     * @see IUserRepository.reIssue
     * @author hjkim
     * @since 2023/02/16
     **/
    suspend operator fun invoke(
        requestParam: ReIssueRequestParam
    ) = repository.reIssue(
        requestParam = requestParam
    )
}