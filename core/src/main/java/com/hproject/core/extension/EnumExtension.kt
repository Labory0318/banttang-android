package com.hproject.core.extension

inline fun <reified EnumType : Enum<EnumType>> enumValueOfOrNull(
    name: String?
): EnumType? = name?.runCatching { enumValueOf<EnumType>(this) }?.getOrNull()