package com.hproject.core.extension

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 40) tag else tag.substring(0, 40)
    }