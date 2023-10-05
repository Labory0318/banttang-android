package com.hproject.banttang.presentation.scene.login

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.hproject.banttang.R
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseViewModel
import com.hproject.banttang.domain.common.define.ErrorCodeDefine
import com.hproject.banttang.domain.user.define.LoginProviderDefine
import com.hproject.banttang.domain.user.entity.LoginResponse
import com.hproject.banttang.domain.user.entity.UserInfoResponse
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.usecase.LoginUseCase
import com.hproject.banttang.domain.user.usecase.GetUserInfoUseCase
import com.hproject.banttang.presentation.scene.join.argument.JoinArgument
import com.hproject.core.domain.authorization.ITokenManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val tokenManager: ITokenManager,
    private val loginUseCase: LoginUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
): AbstractBanttangBaseViewModel() {

    sealed class Event {
        // 카카오 로그인
        object OpenKakaoLogin : Event()
        // 구글 로그인
        object OpenGoogleLogin : Event()
        // 메인 화면으로 이동
        object MoveMainFragment : Event()
        // 동의항목 화면으로 이동
        data class MoveJoinTermsFragment(
            val joinArgument: JoinArgument
        ) : Event()
    }

    /**
     * 로그인
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    private fun login(
        provider: LoginProviderDefine,
        providerAccessToken: String,
        userKey: String,
        name: String?,
        imageUrl: String?
    ) {
        val fcmToken = tokenManager.getFcmToken()
        if (fcmToken.isNullOrEmpty()) {
            Timber.e("fcmToken is null or empty")
            return
        }

        showScreenBlockProgress()

        viewModelScope.launch {
            loginUseCase(
                requestParam = LoginRequestParam(
                    provider = provider,
                    providerAccessToken = providerAccessToken,
                    fcmToken = fcmToken
                )
            ).onSuccess { response ->
                when (response.result) {
                    LoginResponse.Result.LOGIN_SUCCESS -> {
                        tokenManager.setAccessToken(response.data?.accessToken)
                        tokenManager.setRefreshToken(response.data?.refreshToken)

                        if (tokenManager.isValidToken()) {
                            getUserInfo()
                        } else {
                            sendShowToastEvent(R.string.error_common)
                        }
                    }
                    LoginResponse.Result.USER_NOT_FOUND -> {
                        sendEventToView(event = Event.MoveJoinTermsFragment(
                            joinArgument = JoinArgument(
                                provider = provider,
                                providerAccessToken = providerAccessToken,
                                userKey = userKey,
                                name = name,
                                imageUrl = imageUrl
                            )
                        ))
                    }
                    LoginResponse.Result.AUTHENTICATION_FAIL, LoginResponse.Result.FAIL -> {
                        sendShowToastEvent(R.string.error_network)
                    }
                }
            }.onFailure {
                sendShowToastEvent(R.string.error_network)
            }.also {
                hideScreenBlockProgress()
            }
        }
    }

    /**
     * 카카오 로그인
     *
     * @since 2022/01/30
     * @see UserApiClient
     * @see onKakaoLoginCallback
     **/
    fun onKakaoLoginButtonClick() {
        sendEventToView(event = Event.OpenKakaoLogin)
    }

    /**
     * 카카오 로그인 결과
     *
     * @author hjkim
     * @since 2023/02/16
     **/
    fun onKakaoLoginCallback(token: OAuthToken?, error: Throwable?) {
        when {
            //fail
            error != null -> {
                when (error) {
                    is ClientError -> {
                        // client error
                        if (error.reason != ClientErrorCause.Cancelled) {
                            Timber.e("kakao sign [ClientError]. msg is ${error.msg}, reason is ${error.reason}")
                        }
                    }
                }
            }
            //success
            token != null -> {
                showScreenBlockProgress()

                UserApiClient.instance.me { user, userError ->
                    hideScreenBlockProgress()

                    when {
                        userError != null -> userError.printStackTrace()
                        else -> {
                            login(
                                provider = LoginProviderDefine.KAKAO,
                                providerAccessToken = token.accessToken,
                                userKey = user?.id.toString(),
                                name = user?.kakaoAccount?.profile?.nickname,
                                imageUrl = user?.kakaoAccount?.profile?.profileImageUrl
                            )
                        }
                    }
                }
            }
        }
    }

    //----------------------------------------------------------------
    // MARK : Google SignIn
    //----------------------------------------------------------------
    /**
     * 구글 로그인
     *
     * @author hsjun
     * @since 2022/01/30
     * @see GoogleSignInOptions
     * @see onGoogleLoginCallback
     **/
    fun onGoogleLoginClick() {
        sendEventToView(event = Event.OpenGoogleLogin)
    }

    /**
     * 구글 로그인 결과
     *
     * @author hsjun
     * @since 2022/01/30
     **/
    fun onGoogleLoginCallback(result: ActivityResult) {
        Timber.i("resultCode=${result.resultCode}, data=${result.data}")

        when (result.resultCode) {
            // success
            Activity.RESULT_OK -> {
                try {
                    val googleSignInResult: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    Timber.i("---> googleSignInResult is $googleSignInResult")

                    val googleLoginResult = googleSignInResult.getResult(ApiException::class.java)
                    Timber.i("---> googleLoginResult is $googleLoginResult")

                    if (googleLoginResult != null) {
                        Timber.i("---> google account START")
                        Timber.i("id=${googleLoginResult.id}")
                        Timber.i("idToken=${googleLoginResult.idToken}")
                        Timber.i("serverAuthCode=${googleLoginResult.serverAuthCode}")
                        Timber.i("displayName=${googleLoginResult.displayName}")
                        Timber.i("email=${googleLoginResult.email}")
                        Timber.i("photoUrl=${googleLoginResult.photoUrl}")
                        Timber.i("photoUrl=${googleLoginResult.photoUrl?.path}")
                        Timber.i("<--- google account START")

                        val accessToken = googleLoginResult.idToken

                        if (!accessToken.isNullOrEmpty()) {
                            login(
                                provider = LoginProviderDefine.GOOGLE,
                                providerAccessToken = accessToken,
                                userKey = googleLoginResult.id.toString(),
                                name = googleLoginResult.displayName,
                                imageUrl = googleLoginResult.photoUrl?.toString()
                            )
                        } else {
                            Timber.w("token is null")
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun getUserInfo(){
        viewModelScope.launch {
            getUserInfoUseCase.invoke()
                .onSuccess { response ->
                    when(response.result){
                        UserInfoResponse.Result.GET_USER_INFO_SUCCESS-> {
                            sendEventToView(event = Event.MoveMainFragment)
                        }
                        UserInfoResponse.Result.FAIL->{
                            sendShowToastEvent(R.string.error_common)
                        }
                    }
                }
                .onFailure {
                    sendShowToastEvent(R.string.error_common)
                }
        }
    }

}