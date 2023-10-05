package com.hproject.banttang.domain.version.define

/**
 * @author hjkim
 * @since 2023/02/15
 */
enum class VersionStatusDefine {
    // 유효하지 않은 버전인 경우
    INVALID,

    // 필수 업데이트 진행이 필요한 경우
    NEED_UPDATE,

    // 최신 버전
    LATEST,

    // 업데이트는 존재하지만 필수 업데이트가 아닌 경우
    MINOR_UPDATE,

    // 현재 App 버전이 더 높은 경우
    USER_VERSION_HIGHER;
}