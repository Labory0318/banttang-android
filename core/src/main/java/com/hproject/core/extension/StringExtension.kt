package com.hproject.core.extension

import java.text.NumberFormat

fun String.isJsonObject(): Boolean = !isNullOrEmpty() && startsWith("{") && endsWith("}")

fun String.isJsonArray(): Boolean = !isNullOrEmpty() && startsWith("[") && endsWith("]")

fun String.isJson() = isJsonObject() || isJsonArray()

inline fun <reified EnumType : Enum<EnumType>> String?.toEnumOrNull() = enumValueOfOrNull<EnumType>(name = this)

/**
 * formatted number string to number
 * - ex) 1,000 > 1000
 *
 * @author thomas
 * @since 2022/12/08
 **/
fun String?.formattedNumberToString(): Number? {
    if (this.isNullOrEmpty()) {
        return null
    }
    return try {
        val format = NumberFormat.getInstance()
        format.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}