package com.hproject.core.extension

import java.text.NumberFormat

/**
 * number with comma format
 * - ex) 1000 => 1,000
 *
 * @author thomas
 * @since 2022/12/08
 **/
fun Number?.toFormattedString(): String {
    if (this == null) {
        return ""
    }

    return NumberFormat.getNumberInstance().format(this)
}