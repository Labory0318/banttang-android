package com.hproject.banttang.presentation.scene.join.terms.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hproject.banttang.R
import com.hproject.banttang.databinding.TermsDetailFragmentBinding
import com.hproject.banttang.domain.common.define.TermsTypeDefine
import com.hproject.core.presentation.scene.AbstractBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TermsDetailFragment : AbstractBaseFragment<TermsDetailFragmentBinding, TermsDetailFragmentViewModel>(inflate = TermsDetailFragmentBinding::inflate) {

    private val navArgs: TermsDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: TermsDetailFragmentViewModel.Factory

    override val viewModel: TermsDetailFragmentViewModel by viewModels {
        TermsDetailFragmentViewModel.provideFactory(viewModelFactory, navArgs.terms)
    }

    override fun onInitView() {
        binding.viewModel = viewModel
        initWebView()
    }

    override fun onRegisterViewModelObserver() {
    }

    override fun onReceivedViewModelEvent(event: Any) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.termsWebView.apply {
            settings.apply {
                javaScriptEnabled = true
                useWideViewPort = true
                displayZoomControls = false
                builtInZoomControls = false
            }
            webViewClient = WebViewClient()
            val termsUrl: String = getString(
                when (navArgs.terms) {
                    TermsTypeDefine.SERVICE -> R.string.banttang_service_terms_url
                    TermsTypeDefine.PRIVACY_POLICY -> R.string.banttang_privacy_policy_url
                    TermsTypeDefine.GPS -> R.string.banttang_gps_terms_url
                }
            )
            loadUrl(termsUrl)
        }
    }
}