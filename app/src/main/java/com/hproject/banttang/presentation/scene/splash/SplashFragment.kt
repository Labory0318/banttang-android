package com.hproject.banttang.presentation.scene.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import com.hproject.banttang.R
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseFragment
import com.hproject.banttang.databinding.SplashFragmentBinding
import com.hproject.banttang.define.AppDefine
import com.hproject.banttang.presentation.common.extension.alertDialog
import com.hproject.banttang.presentation.common.extension.moveToMarket
import com.hproject.core.presentation.extension.applyFullScreen
import com.hproject.core.presentation.extension.clearFullScreen
import com.hproject.core.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : AbstractBanttangBaseFragment<SplashFragmentBinding, SplashFragmentViewModel>(
    inflate = SplashFragmentBinding::inflate
) {
    companion object {
        private const val SPLASH_DELAY_TIME_MILLIS: Long = 500L
    }

    override val viewModel: SplashFragmentViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        applyFullScreen()

        super.onViewCreated(view, savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            this::onPermissionCallback
        )

        Handler(Looper.getMainLooper()).postDelayed({
            checkPermission()
        }, SPLASH_DELAY_TIME_MILLIS)
    }

    override fun onInitView() {
        binding.viewModel = viewModel
    }

    override fun onRegisterViewModelObserver() {

    }

    override fun onReceivedViewModelEvent(event: Any) {
        (event as? SplashFragmentViewModel.Event)?.let {
            when (event) {
                // 로그인 화면으로 이동
                is SplashFragmentViewModel.Event.MoveLoginFragment -> {
                    moveToLoginFragment()
                }
                is SplashFragmentViewModel.Event.MoveMainFragment -> {
                    moveToMainFragment()
                }
                // 필수 업데이트 팝업 표출
                is SplashFragmentViewModel.Event.ShowAppUpdateDialog -> {
                    showNeedUpdateDialog()
                }
                // 버전 체크 재 시도 팝업 표출
                is SplashFragmentViewModel.Event.ShowCheckAppVersionRetryDialog -> {
                    showCheckAppVersionRetryDialog()
                }
            }
        }
    }

    override fun onDestroy() {
        clearFullScreen()

        super.onDestroy()
    }

    // 권한 체크
    private fun checkPermission() {
        val isPermissionGranted = PermissionUtils.isAllPermissionGranted(
            context = requireContext(),
            permissions = AppDefine.NEED_PERMISSIONS
        )

        if (isPermissionGranted) {
            viewModel.getFcmToken()
        } else {
            permissionLauncher.launch(AppDefine.NEED_PERMISSIONS)
        }
    }

    private fun onPermissionCallback(result: Map<String, Boolean>) {
        Timber.i("result is $result")

        viewModel.getFcmToken()
    }

    /**
     * 필수 업데이트 진행 팝업 출력
     *
     * @author hjkim
     * @since 2023/03/23
    **/
    private fun showNeedUpdateDialog() {
        alertDialog(
            title = R.string.app_name,
            message = R.string.version_need_update_msg,
        ) {
            positiveButton(R.string.update) {
                activity?.moveToMarket()
                activity?.finish()
            }
            negativeButton(R.string.cancel) {
                activity?.finishAffinity()
            }
        }
    }

    /**
     * 버전 체크 실패 시, 재시도 팝업 출력
     *
     * @author hjkim
     * @since 2023/03/23
     **/
    private fun showCheckAppVersionRetryDialog() {
        alertDialog(
            title = R.string.app_name,
            message = R.string.version_retry_msg
        ) {
            positiveButton(R.string.retry) {
                viewModel.versionCheck()
            }
            negativeButton(R.string.cancel) {
                activity?.finishAffinity()
            }
        }
    }

    /**
     * 로그인 화면으로 이동
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    private fun moveToLoginFragment() {
        navigate(
            directions = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        )
    }

    /**
     * 메인 화면으로 이동
     *
     * @author hjkim
     * @since 2023/03/23
     **/
    private fun moveToMainFragment() {
        navigate(
            directions = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        )
    }
}