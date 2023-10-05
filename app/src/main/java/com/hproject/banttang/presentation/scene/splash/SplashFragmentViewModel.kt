package com.hproject.banttang.presentation.scene.splash

import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.hproject.banttang.BuildConfig
import com.hproject.banttang.R
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseViewModel
import com.hproject.banttang.domain.user.entity.LoginResponse
import com.hproject.banttang.domain.user.entity.UserInfoResponse
import com.hproject.banttang.domain.user.request_param.ReIssueRequestParam
import com.hproject.banttang.domain.user.usecase.GetUserInfoUseCase
import com.hproject.banttang.domain.user.usecase.ReIssueUseCase
import com.hproject.banttang.domain.version.define.VersionStatusDefine
import com.hproject.banttang.domain.version.entity.VersionCheckResponse
import com.hproject.banttang.domain.version.request_param.VersionCheckRequestParam
import com.hproject.banttang.domain.version.usecase.VersionCheckUseCase
import com.hproject.core.data.data_source.local.data_store.PreferenceManager
import com.hproject.core.domain.authorization.ITokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    // 버전 정보 조회하기
    private val versionCheckUseCase: VersionCheckUseCase,
    // 자동로그인
    private val reIssueUseCase: ReIssueUseCase,
    // 사용자 정보 조회
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val tokenManager: ITokenManager,
    private val preferenceManager: PreferenceManager,
) : AbstractBanttangBaseViewModel() {

    sealed class Event {
        // 로그인 화면으로 이동
        object MoveLoginFragment : Event()
        // 메인 화면으로 이동
        object MoveMainFragment : Event()
        // 필수 업데이트 팝업 표출
        object ShowAppUpdateDialog: Event()
        // 버전 체크 재 시도 팝업 표출
        object ShowCheckAppVersionRetryDialog: Event()
    }

    /**
     * FCM 토큰 얻기
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("fcmToken is ${task.result}")
                tokenManager.setFcmToken(task.result)
            } else {
                Timber.e("FirebaseMessaging issue token failed.")
            }

            versionCheck()
        }
    }

    /**
     * 버전 체크
     *
     * @author hjkim
     * @since 2023/02/15
     **/
    fun versionCheck() {
        viewModelScope.launch {
            versionCheckUseCase(
                requestParam = VersionCheckRequestParam(
                    version = BuildConfig.VERSION_NAME
                )
            ).onSuccess { response ->
                when (response.result) {
                    VersionCheckResponse.Result.GET_VERSION_INFO_SUCCESS -> {
                        when (response.data?.applicationCheckResultCode) {
                            /* 필수 업데이트 */
                            VersionStatusDefine.NEED_UPDATE -> {
                                showAppUpdateDialog()
                            }
                            /* 앱 사용 가능 */
                            VersionStatusDefine.MINOR_UPDATE,
                            VersionStatusDefine.LATEST,
                            VersionStatusDefine.USER_VERSION_HIGHER -> {
                                //TODO 토큰값을 local에 저장하지 않음으로 인한 자동로그인 기능 이슈. DataStore구현 후 자동로그인 기능 적용 필요. 현재는 로그인페이지로 이동 처리
                                //reIssue()
                                moveToLoginPage()
                            }
                            else -> {
                                showCheckAppVersionRetryDialog()
                            }
                        }
                    }
                    VersionCheckResponse.Result.FAIL -> {
                        showCheckAppVersionRetryDialog()
                    }
                }
            }.onFailure {
                showCheckAppVersionRetryDialog()
            }
        }
    }

    /**
     * 자동 로그인
     *
     * @author hjkim
     * @since 2023/03/23
     **/
    fun reIssue() {
        viewModelScope.launch {
            reIssueUseCase(
                requestParam = ReIssueRequestParam(
                    accessToken = tokenManager.getAccessToken()!!,
                    refreshToken = tokenManager.getRefreshToken()!!,
                    fcmToken = tokenManager.getFcmToken()!!
                )
            ).onSuccess { response ->
                when (response.result) {
                    LoginResponse.Result.LOGIN_SUCCESS -> {
                        tokenManager.setAccessToken(response.data?.accessToken)
                        tokenManager.setRefreshToken(response.data?.refreshToken)

                        if (tokenManager.isValidToken()) {
                            me()
                        } else {
                            moveToLoginPage()
                        }
                    }
                    else -> {
                        moveToLoginPage()
                    }
                }
            }.onFailure {
                moveToLoginPage()
            }
        }
    }

    /**
     * 사용자 정보 조회
     *
     * @author hjkim
     * @since 2023/03/23
     **/
    private fun me() {
        viewModelScope.launch {
            getUserInfoUseCase.invoke()
                .onSuccess { response ->
                    when (response.result) {
                        UserInfoResponse.Result.GET_USER_INFO_SUCCESS -> {
                            sendEventToView(event = Event.MoveMainFragment)
                        }
                        UserInfoResponse.Result.FAIL -> {
                            sendEventToView(event = Event.MoveLoginFragment)
                        }
                    }
                }
                .onFailure {
                    sendShowToastEvent(R.string.error_common)
                }
        }
    }

    /**
     * 서버에서 전달 받은 버전이 정상적이지 않을 경우
     *
     * @author hsjun
     * @since 2022/10/25
     **/
    private fun showCheckAppVersionRetryDialog() {
        sendEventToView(event = Event.ShowCheckAppVersionRetryDialog)
    }

    /**
     * 필수 업데이트 팝업 표출
     *
     * @author hsjun
     * @since 2022/10/25
     **/
    private fun showAppUpdateDialog() {
        sendEventToView(event = Event.ShowAppUpdateDialog)
    }

    /**
     * 로그인 페이지 이동
     *
     * @author hsjun
     * @since 2022/06/17
     **/
    private fun moveToLoginPage() {
        sendEventToView(event = Event.MoveLoginFragment)
    }
}