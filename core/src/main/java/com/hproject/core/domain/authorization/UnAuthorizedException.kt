package com.hproject.core.domain.authorization

class UnAuthorizedException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)