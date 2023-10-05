package com.hproject.banttang.domain.version.usecase

import com.hproject.banttang.domain.version.repository.IVersionCheckRepository
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam
import javax.inject.Inject

/**
 * 버전 정보 조회 UseCase
 *
 * @author hjkim
 * @since 2023/02/15
 **/
class VersionCheckUseCase @Inject constructor(
    private val repository: IVersionCheckRepository
) {
    /**
     * 버전 정보 조회
     *
     * @see IVersionCheckRepository.fetchTopBanner
     * @author hjkim
     * @since 2023/02/15
     **/
    suspend operator fun invoke(
        requestParam: VersionCheckRequestParam
    ) = repository.versionCheck(
        requestParam = requestParam
    )
}