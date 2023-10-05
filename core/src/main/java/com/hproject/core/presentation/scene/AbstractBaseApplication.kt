package com.hproject.core.presentation.scene

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.hproject.core.BuildConfig
import com.hproject.core.extension.TAG
import com.hproject.core.utils.LogTree
import timber.log.Timber

/**
 * BaseApplication
 *
 * @author thomas
 * @since 2022/11/03
 **/
abstract class AbstractBaseApplication : Application() {
    // 활성화 된 (현재 보여지고 있는) Activity 정보
    var activeActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        initLogger()
        registerActivityLifecycleCallbacks()
    }

    /**
     * Application Logger 초기화
     * - [BuildConfig.DEBUG] 경우에만 초기화 진행, Release Mode 에서는 로그 출력 제한
     *
     * @author thomas
     * @since 2022/11/03
     **/
    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(LogTree(this::class.java.simpleName))
        }
    }

    /**
     * Activity Lifecycle 모니터링 콜백 등록
     *
     * @author thomas
     * @since 2022/11/03
     **/
    private fun registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Timber.i("[onActivityCreated]: ${activity.TAG}")
            }

            override fun onActivityStarted(activity: Activity) {
                Timber.i("[onActivityStarted]: ${activity.TAG}")
            }

            override fun onActivityResumed(activity: Activity) {
                Timber.i("[onActivityResumed]: ${activity.TAG}")
                activeActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                Timber.i("[onActivityPaused]: ${activity.TAG}")
                activeActivity = null
            }

            override fun onActivityStopped(activity: Activity) {
                Timber.i("[onActivityStopped]: ${activity.TAG}]")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Timber.i("[onActivitySaveInstanceState]: ${activity.TAG}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Timber.i("[onActivityDestroyed]: ${activity.TAG}")
            }
        })
    }
}