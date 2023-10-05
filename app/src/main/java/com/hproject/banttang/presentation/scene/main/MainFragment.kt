package com.hproject.banttang.presentation.scene.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseFragment
import com.hproject.banttang.data.user.manager.UserManager
import com.hproject.banttang.databinding.MainFragmentBinding
import com.hproject.core.data.authorization.TokenManager
import com.hproject.core.presentation.scene.AbstractBaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : AbstractBanttangBaseFragment<MainFragmentBinding, MainFragementViewModel>(
    inflate = MainFragmentBinding::inflate
) {
    @Inject
    lateinit var userInfoManager : UserManager

    override val viewModel: MainFragementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onInitView() {
        binding.viewModel = viewModel
    }

    override fun onRegisterViewModelObserver() {

    }

    override fun onReceivedViewModelEvent(event: Any) {
        
    }
}