package com.hproject.banttang.presentation.scene.join.terms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.hproject.banttang.R
import com.hproject.banttang.databinding.JoinTermsFragmentBinding
import com.hproject.banttang.domain.common.define.TermsTypeDefine
import com.hproject.core.presentation.scene.AbstractBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs
import com.hproject.banttang.presentation.scene.join.argument.JoinArgument
import com.hproject.core.presentation.scene.AbstractBaseViewModel
import javax.inject.Inject

@AndroidEntryPoint
class JoinTermsFragment: AbstractBaseFragment<JoinTermsFragmentBinding, JoinTermsFragmentViewModel>(inflate = JoinTermsFragmentBinding::inflate) {

    private val navArgs: JoinTermsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: JoinTermsFragmentViewModel.Factory

    override val viewModel: JoinTermsFragmentViewModel by viewModels {
        JoinTermsFragmentViewModel.provideFactory(viewModelFactory, navArgs.joinArgument)
    }

    override fun onInitView() {
        setBinding()
    }

    private fun setBinding(){
        with(binding){
            joinTermsViewModel = viewModel
            lifecycleOwner  = this@JoinTermsFragment
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onRegisterViewModelObserver() {

    }

    override fun onReceivedViewModelEvent(event: Any) {
        (event as? JoinTermsFragmentViewModel.JoinTermsEvent)?.let {
            when (event) {
                is JoinTermsFragmentViewModel.JoinTermsEvent.MoveTermsDetailFragment -> {
                    moveToJoinTermsFragment(event.termsTypeDefine)
                }
                is JoinTermsFragmentViewModel.JoinTermsEvent.MoveJoinProfileFragment -> {
                    moveToJoinProfileFragment(event.joinArgument)
                }
            }
        }
    }
    private fun moveToJoinTermsFragment(termsDefine: TermsTypeDefine) {
        activity?.findNavController(R.id.main_navigation_container_view)?.navigate(
            directions = JoinTermsFragmentDirections.actionJoinTermsFragmentToJoinTermsDetailFragment(
                terms = termsDefine
            )
        )
    }

    private fun moveToJoinProfileFragment(joinArgument: JoinArgument) {
        activity?.findNavController(R.id.main_navigation_container_view)?.navigate(
            directions = JoinTermsFragmentDirections.actionJoinTermsFragmentToJoinProfileFragment(
                joinArgument = joinArgument
            )
        )
    }
}