package com.hproject.banttang.base.presentation.scene

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.hproject.core.presentation.scene.AbstractBaseActivity

/**
 * StyleSellerApp BaseActivity
 *
 * @param layoutResId layout resource id
 * @see AbstractBaseActivity
 * @author hjkim
 * @since 2023/02/13
 **/
abstract class AbstractBanttangBaseActivity<VDB : ViewDataBinding>(
    @LayoutRes layoutResId: Int
) : AbstractBaseActivity<VDB>(
    layoutResId = layoutResId
)