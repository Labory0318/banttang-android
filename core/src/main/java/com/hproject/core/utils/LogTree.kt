package com.hproject.core.utils

import timber.log.Timber

class LogTree(private val prefix: String) : Timber.DebugTree() {
    /**
     * Log 형식 설정
     *
     * @author thomas
     * @since 2022/11/03
     **/
    override fun createStackElementTag(element: StackTraceElement): String = "$prefix: (${element.fileName}:${element.lineNumber}) [${element.methodName}]"
}