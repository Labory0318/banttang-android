package com.hproject.banttang.define

import android.Manifest

/**
 * Banttang App Constants
 *
 * @author hsjun
 * @since 2022/02/11
 */
object AppDefine {
    val NEED_PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}