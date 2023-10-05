package com.hproject.core.presentation.scene

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.hproject.core.extension.TAG
import com.hproject.core.presentation.component.ProgressDialog
import com.hproject.core.presentation.extension.showToast
import timber.log.Timber


/**
 * BaseFragment
 *
 * @author thomas
 * @since 2022/11/03
 **/
abstract class AbstractBaseFragment<VDB : ViewDataBinding, VM : AbstractBaseViewModel>(
    private val inflate: Inflate<VDB>
) : Fragment(),
    IBaseScene<VDB, VM> {
    private val childTag = this.TAG

    private var _binding: VDB? = null
    override val binding: VDB get() = _binding!!

    private lateinit var mBackPressedCallback: OnBackPressedCallback

    private var mScreenBlockProgressDialog: ProgressDialog? = null

    override fun onRegisterResultObserver() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Timber.i(childTag)

        registerBackPressedCallback()
    }

    // MARK : Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i(childTag)
    }

    /**
     * [Android Developer 권장 사항]
     * - [_binding]의 경우 [onCreateView]에서 할당 [onDestroyView] 해제
     *
     * @author thomas
     * @since 2022/11/07
     **/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        Timber.i(childTag)

        if (_binding == null) {
            _binding = inflate(inflater, container, false)
            _binding?.lifecycleOwner = viewLifecycleOwner

            Timber.i("$childTag, initialize view. onInitView")

            onInitView()
        }

        registerBaseObserver()
        onRegisterViewModelObserver()
        onRegisterResultObserver()

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Timber.i("${childTag}, savedInstanceState=$savedInstanceState")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i(childTag)
    }

    override fun onResume() {
        super.onResume()

        Timber.i(childTag)
    }

    override fun onStart() {
        super.onStart()

        Timber.i(childTag)
    }

    override fun onPause() {
        super.onPause()

        Timber.i(childTag)
    }

    override fun onDestroyView() {
        destroyScreenBlockProgress()

        super.onDestroyView()

        Timber.i(childTag)

        /*
         * [Android Developer 권장 사항]
         * - [!] Fragment Binding > memory 효율성을 위해, [onDestroyView] 에서 [_binding] 해제 필수 [!]
         * - Fragment 경우 [[onCreateView] > [onDestroyView] > [onCreateView] > ...] 반복 되기 때문에
         * [onCreateView] 생성 / [onDestroyView] 해제 방식으로 memory leak 방지 필요.
         */
//        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.i(childTag)
    }

    override fun onDetach() {
        super.onDetach()

        Timber.i(childTag)

        mBackPressedCallback.remove()
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
                navigateUp()
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

    // MARK : Navigation
    /**
     * 화면 이동
     *
     * @author thomas
     * @since 2022/11/04
     **/
    open fun navigate(
        directions: NavDirections,
        navOptions: NavOptions? = null
    ) {
        findNavController().navigate(
            directions = directions,
            navOptions = navOptions
        )
    }

    /**
     * 이전 화면으로 이동
     *
     * @author thomas
     * @since 2022/11/04
     **/
    open fun navigateUp() {
        val result = findNavController().navigateUp()
        Timber.i("navigateUp result is $result")

        if (!result) {
            activity?.finish()
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
     * onBackPressed event
     *
     * @author thomas
     * @since 2022/11/08
     **/
    open fun onBackPressed() {
        Timber.i("back button pressed. from $childTag")
        navigateUp()
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

    /**
     * System Back Key Callback 등록
     *
     * @author thomas
     * @since 2022/11/08
     **/
    private fun registerBackPressedCallback() {
        mBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, mBackPressedCallback)
    }
}