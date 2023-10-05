package com.hproject.core.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * @author hsjun
 * @since 2022/02/18
 */
object PermissionUtils {

    /**
     * check permission is granted
     *
     * @author hsjun
     * @since 2021-02-18
     */
    private fun isPermissionGranted(
        context: Context,
        permission: String
    ): Boolean {
        return (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED)
    }

    fun isAllPermissionGranted(
        context: Context,
        permissions: Array<String>
    ): Boolean {
        var isGranted = true

        for (permission in permissions) {
            if (!isPermissionGranted(context, permission)) {
                isGranted = false
                break
            }
        }

        return isGranted
    }
}