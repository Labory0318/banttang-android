package com.hproject.banttang

import android.os.Bundle
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseActivity
import com.hproject.banttang.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbstractBanttangBaseActivity<MainActivityBinding>(
    layoutResId = R.layout.main_activity
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Banttang)

        super.onCreate(savedInstanceState)
    }

    override fun onInitBinding() {

    }
}