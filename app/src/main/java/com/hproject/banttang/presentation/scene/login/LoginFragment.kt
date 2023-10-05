package com.hproject.banttang.presentation.scene.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hproject.banttang.R
import com.hproject.banttang.databinding.LoginFragmentBinding
import com.hproject.banttang.presentation.scene.join.argument.JoinArgument
import com.hproject.core.presentation.scene.AbstractBaseFragment
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : AbstractBaseFragment<LoginFragmentBinding, LoginFragmentViewModel>(
    inflate = LoginFragmentBinding::inflate
) {
    override val viewModel: LoginFragmentViewModel by viewModels()

    private var mGoogleLoginRequest: ActivityResultLauncher<Intent>? = null
    private var mGoogleClient: GoogleSignInClient? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogleLogin()
    }

    override fun onInitView() {
        binding.viewModel = viewModel
    }

    override fun onRegisterViewModelObserver() {

    }

    override fun onReceivedViewModelEvent(event: Any) {
        (event as? LoginFragmentViewModel.Event)?.let {
            when (event) {
                is LoginFragmentViewModel.Event.OpenKakaoLogin -> {
                    kakaoLogin()
                }
                is LoginFragmentViewModel.Event.OpenGoogleLogin -> {
                    googleLogin()
                }
                is LoginFragmentViewModel.Event.MoveJoinTermsFragment -> {
                    moveToJoinTermsFragment(event.joinArgument)
                }
                is LoginFragmentViewModel.Event.MoveMainFragment -> {
                    moveToMainFragment()
                }
            }
        }
    }

    /**
     * 카카오 로그인
     *
     * @author hsjun
     * @since 2022/06/25
     **/
    private fun kakaoLogin() {
        context?.let { context ->
            val client = UserApiClient.instance

            // KakaoTalk App Exist
            if (client.isKakaoTalkLoginAvailable(context = context)) {
                // [2022/01/30] hsjun: open kakao app for app login
                client.loginWithKakaoTalk(
                    context = context,
                    callback = viewModel::onKakaoLoginCallback
                )
            }
            // not installed
            else {
                // [2022/01/30] hsjun: open web view for account login
                client.loginWithKakaoAccount(
                    context = context,
                    callback = viewModel::onKakaoLoginCallback
                )
            }
        }
    }

    /**
     * 구글 로그인 초기화
     *
     * @author hsjun
     * @since 2022/06/25
     **/
    private fun initGoogleLogin() {
        mGoogleLoginRequest = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            viewModel.onGoogleLoginCallback(it)
            mGoogleClient?.signOut()
        }
    }

    /**
     * 구글 로그인
     *
     * @author hsjun
     * @since 2022/06/25
     **/
    private fun googleLogin() {
        activity?.let { activity ->
            val serverClientId = getString(R.string.google_server_client_id)

            mGoogleClient = GoogleSignIn.getClient(
                activity,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(serverClientId)
                    .requestServerAuthCode(serverClientId)
                    .requestProfile()
                    .requestId()
                    .build()
            )

            mGoogleLoginRequest?.launch(mGoogleClient?.signInIntent)
        }
    }

    /**
     * 약관동의 화면으로 이동
     *
     * @author hjkim
     * @since 2023/02/17
     **/
    private fun moveToJoinTermsFragment(joinArgument: JoinArgument) {
        activity?.findNavController(R.id.main_navigation_container_view)?.navigate(
            directions = LoginFragmentDirections.actionLoginFragmentToJoinTermsFragment(
                joinArgument = joinArgument
            )
        )
    }

    /**
     * 메인 화면으로 이동
     *
     * @author hjkim
     * @since 2023/02/17
     **/
    private fun moveToMainFragment() {
        activity?.findNavController(R.id.main_navigation_container_view)?.navigate(
            directions = LoginFragmentDirections.actionLoginFragmentToMainFragment()
        )
    }
}