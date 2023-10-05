package com.hproject.banttang.presentation.scene.join.argument

import android.os.Parcelable
import com.hproject.banttang.domain.user.define.LoginProviderDefine
import kotlinx.parcelize.Parcelize

/**
 * @author hjkim
 * @since 2023/02/17
 */
@Parcelize
data class JoinArgument(
    val provider: LoginProviderDefine,
    val providerAccessToken: String,
    val userKey: String,
    var name: String? = null,
    var imageUrl: String? = null
) : Parcelable