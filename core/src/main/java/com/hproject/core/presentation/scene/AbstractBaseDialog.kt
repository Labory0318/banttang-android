package com.hproject.core.presentation.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.hproject.core.extension.TAG
import com.hproject.core.presentation.component.ProgressDialog
import com.hproject.core.presentation.extension.applyTransparentBackground
import com.hproject.core.presentation.extension.showToast
import timber.log.Timber

abstract class AbstractBaseDialog<VDB : ViewDataBinding, VM : AbstractBaseViewModel>(
    private val inflate: Inflate<VDB>
) : DialogFragment(),
    IBaseScene<VDB, VM> {
    private val childTag = this.TAG

    private var _binding: VDB? = null
    override val binding: VDB get() = _binding!!

    private var mScreenBlockProgressDialog: ProgressDialog? = null

    open fun onInitDialog() {
        applyTransparentBackground()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.i(childTag)

        _binding = inflate(inflater, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i(childTag)

        onInitDialog()

        registerBaseObserver()

        onInitView()
        onRegisterViewModelObserver()
        onRegisterResultObserver()
    }

    override fun onDestroyView() {
        destroyScreenBlockProgress()

        super.onDestroyView()

        Timber.i(childTag)

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.i(childTag)
    }

    // MARK : Functions
    /**
     * [AbstractBaseViewModel]로 부터 전달 받은 Event
     *
     * @author thomas
     * @since 2022/11/03
     **/
    open fun onReceivedViewModelBaseEvent(event: AbstractBaseViewModel.Event) {
        Timber.i("event=$event")

        when (event) {
            is AbstractBaseViewModel.Event.NavigateUp -> {
                dismiss()
            }
            is AbstractBaseViewModel.Event.ShowScreenBlockProgress -> {
                showScreenBlockProgress()
            }
            is AbstractBaseViewModel.Event.HideScreenBlockProgress -> {
                hideScreenBlockProgress()
            }
            is AbstractBaseViewModel.Event.ShowToastMessage -> {
                if (event.message != null) {
                    showToast(message = event.message)
                }
                else if (event.messageRes != null) {
                    showToast(messageResId = event.messageRes)
                }
            }
        }
    }

    //--------------------------------------------
    // MARK : Progress
    //--------------------------------------------
    open fun createScreenBlockProgress(): ProgressDialog? {
        var progressBarDialog: ProgressDialog? = null

        context?.let { context ->
            progressBarDialog = ProgressDialog(context = context)
        }

        return progressBarDialog
    }

    /**
     * Show Screen Block Progress
     *
     * @author thomas
     * @since 2023/01/02
     **/
    open fun showScreenBlockProgress() {
        if (mScreenBlockProgressDialog == null) {
            mScreenBlockProgressDialog = createScreenBlockProgress()
        }

        mScreenBlockProgressDialog?.show()
    }

    /**
     * Hide Screen Block Progress
     *
     * @author thomas
     * @since 2023/01/02
     **/
    open fun hideScreenBlockProgress() {
        mScreenBlockProgressDialog?.dismiss()
    }

    open fun destroyScreenBlockProgress() {
        mScreenBlockProgressDialog?.dismiss()
        mScreenBlockProgressDialog = null
    }

    /**
     * [viewModel] BaseEvent Observer 등록
     * - 공통으로 사용할 Observer 등록 시 사용.
     * - 1. event observer 등록. [onReceivedViewModelBaseEvent]으로 callback 전달 됨.
     *
     * @author thomas
     * @since 2022/11/03
     **/
    @Suppress("UNCHECKED_CAST")
    private fun registerBaseObserver() {
        viewModel.eventMessage.observe(viewLifecycleOwner) { eventMessage ->
            eventMessage.getContentIfNotHandled()?.let { event ->
                when (event) {
                    is AbstractBaseViewModel.Event -> {
                        onReceivedViewModelBaseEvent(event = event)
                    }
                    else -> {
                        onReceivedViewModelEvent(event = event)
                    }
                }
            }
        }
    }
}