package com.hproject.banttang.base.presentation.scene

import androidx.databinding.ViewDataBinding
import com.hproject.banttang.R
import com.hproject.core.presentation.component.ProgressDialog
import com.hproject.core.presentation.scene.AbstractBaseFragment
import com.hproject.core.presentation.scene.AbstractBaseViewModel
import com.hproject.core.presentation.scene.Inflate

abstract class AbstractBanttangBaseFragment<VDB : ViewDataBinding, VM : AbstractBaseViewModel>(
    inflate: Inflate<VDB>
) : AbstractBaseFragment<VDB, VM>(
    inflate = inflate
){
    /**
     * 공용 ScreenBlock UI 재정의
     *
     * @author hjkim
     * @since 2023/02/13
     **/
    override fun createScreenBlockProgress(): ProgressDialog? {
        return context?.let { context ->
            ProgressDialog(context, R.layout.full_screen_progress_bar_view)
        }
    }
}