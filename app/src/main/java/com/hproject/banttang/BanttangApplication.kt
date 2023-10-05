package com.hproject.banttang

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.hproject.core.domain.authorization.ITokenManager
import com.hproject.core.presentation.scene.AbstractBaseApplication
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Banttang Application
 *
 * @author hjkim
 * @since 2023/02/13
 * @see AbstractBaseApplication
 **/
@HiltAndroidApp
class BanttangApplication: AbstractBaseApplication() {
    override fun onCreate() {
        super.onCreate()

        initFirebase()
        initKakaoSdk()
    }

    /**
     * @author hsjun
     * @since 2022/06/21
     **/
    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }

    /**
     * [KakaoSdk] 초기화
     *
     * @author hsjun
     * @since 2022/09/20
     **/
    private fun initKakaoSdk() {
        // initialize kakao sdk for kakao social login
        KakaoSdk.init(context = this, appKey = getString(R.string.kakao_app_key), loggingEnabled = true)
        // [2022/02/11] hsjun: get kakao hash key
//        Timber.i("KAKAO HASH KEY = ${Utility.getKeyHash(this)}")
    }
}