package com.hproject.core.presentation.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.GravityInt
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * 요청한 화면으로 부터 결과를 받기 위한 Observer 등록
 * - Observer onDestroy 에서 자동으로 제거 됨.
 *
 * @param key       결과를 전달 받기 위한 Key
 * @param onResult  callback function
 * @author thomas
 * @since 2022/11/04
 **/
fun <T> Fragment.registerResultObserver(
    key: String = "data",
    onResult: (result: T) -> Unit
) {
    // navBackStackEntry 요청하는 자기 자신, currentBackStack
    val navBackStackEntry = findNavController().currentBackStackEntry

    val observer = LifecycleEventObserver { _, event ->
        // 요청화면으로 부터 돌아왔을 때 onResume()
        if (event == Lifecycle.Event.ON_RESUME
            // 요청한 결과가 존재할 경우
            && navBackStackEntry?.savedStateHandle?.contains(key) == true
        ) {
            // 결과 전달
            val result = navBackStackEntry.savedStateHandle.get<T>(key)
            Timber.i("RESULT:: onResult, result is [$result]")
            result?.let(onResult)
            // 결과 전달 후 savedStateHandle 에 저장된 값 제거
            navBackStackEntry.savedStateHandle.remove<T>(key)
        }
    }
    Timber.i("RESULT:: key[$key], start to observe.")
    navBackStackEntry?.lifecycle?.addObserver(observer)

    // Observer onDestroy 에서 제거, 매번 각 화면에서 처리하지 않고 lifeCycle 에 따라 자동적으로 제거 되도록 처리
    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        // onDestroy 에서 observer 제거
        if (event == Lifecycle.Event.ON_DESTROY) {
            // lifeCycleObserver 제거
            navBackStackEntry?.lifecycle?.removeObserver(observer)
        }
    })
}

/**
 * 이전 화면 즉, 요청 화면으로 전달 할 값 설정
 * - [registerResultObserver] Callback 으로 전달하기 위한 결과 값 설정
 *
 * @author thomas
 * @since 2022/11/04
 **/
fun <T> Fragment.setResult(
    key: String = "data",
    value: T
) {
    Timber.i("RESULT: from=${this::class.java.simpleName}, key=$key, result=$value")
    // 이전 화면 즉, 요청 화면 [previousBackStackEntry] 으로 전달 할 값 설정
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

/**
 * enable full screen
 *
 * @author thomas
 * @since 2022/11/08
 **/
fun Fragment.applyFullScreen() {
    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * disable full screen
 *
 * @author thomas
 * @since 2022/11/08
 **/
fun Fragment.clearFullScreen() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * Dialog 간단 출력
 *
 * @author thomas
 * @since 2023/01/18
 **/
fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, null)
}

/**
 * 다이얼로그 뒷 배경 Dim 적용
 *
 * @author thomas
 * @since 2023/01/13
 **/
fun DialogFragment.applyDim() {
    dialog?.window?.setDimAmount(1.0f)
}

/**
 * 다이얼로그 뒷 배경 Dim 제거
 *
 * @author thomas
 * @since 2023/01/13
 **/
fun DialogFragment.clearDim() {
    dialog?.window?.setDimAmount(0f)
}

/**
 * 다이얼로그 Dim 수치 변경
 *
 * @author thomas
 * @since 2023/01/18
 **/
fun DialogFragment.setDimAmount(amount: Float) {
    dialog?.window?.setDimAmount(amount)
}

/**
 * 다이얼로그 배경 투명 처리
 *
 * @author thomas
 * @since 2023/01/17
 **/
fun DialogFragment.applyTransparentBackground() {
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

/**
 * MatchParentWidth
 *
 * @author thomas
 * @since 2023/01/30
 **/
fun DialogFragment.applyMatchParentWidth() {
    dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}

/**
 * 토스트 메세지 출력
 *
 * @param message       출력 메세지
 * @param isLongToast   출력 시간이 긴 토스트 여부 (default: false)
 * @author thomas
 * @since 2022/11/03
 **/
fun Fragment.showToast(
    message: String,
    isLongToast: Boolean = false
) = context?.showToast(
    message = message,
    isLongToast = isLongToast
)

/**
 * 토스트 메세지 출력
 *
 * @param messageResId  출력 메세지
 * @param isLongToast   출력 시간이 긴 토스트 여부 (default: false)
 * @author thomas
 * @since 2022/11/03
 **/
fun Fragment.showToast(
    messageResId: Int,
    isLongToast: Boolean = false
) = context?.showToast(
    messageResId = messageResId,
    isLongToast = isLongToast
)


fun Fragment.showSnackbar(
    view: View? = this.view,
    message: CharSequence,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT,
    gravity: Int? = null,
    @ColorRes background: Int? = null
) {
    view?.let {
        Snackbar.make(view, message, duration).also { snackbar ->
            gravity?.let {
                when(val layout = snackbar.view.layoutParams) {
                    is CoordinatorLayout.LayoutParams -> {
                        layout.gravity = gravity
                    }
                    is FrameLayout.LayoutParams -> {
                        layout.gravity = gravity
                    }
                }
            }
            background?.let {
                snackbar.setBackgroundTint(ResourcesCompat.getColor(resources, background, null))
            }
        }.show()
    } ?: run {
        Timber.w("not available to make snackBar from this view")
    }
}

fun Fragment.showSnackbar(
    view: View? = this.view,
    @StringRes messageRes: Int,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT,
    @GravityInt gravity: Int? = null,
    @ColorRes background: Int? = null
) = showSnackbar(
    view = view,
    message = getString(messageRes),
    duration = duration,
    gravity = gravity,
    background = background
)