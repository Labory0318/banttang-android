package com.hproject.core.presentation.scene


import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class AbstractBaseActivity<VDB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {
    private var _binding: VDB? = null

    // binding external variable
    val binding: VDB get() = _binding!!

    /**
     * 초기화
     *
     * @author thomas
     * @since 2022/11/03
     **/
    abstract fun onInitBinding()

    // MARK : Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this

        onInitBinding()
    }
}