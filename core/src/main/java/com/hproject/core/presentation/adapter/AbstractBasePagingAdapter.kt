package com.hproject.core.presentation.adapter

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hproject.core.presentation.extension.isError
import com.hproject.core.presentation.extension.isLoading
import com.hproject.core.presentation.extension.isReachedEndOfPagination

typealias OnPagingLoadStateListener = (isLoading: Boolean) -> Unit
typealias OnPagingErrorStateListener = (error: Throwable) -> Unit
typealias OnPagingEmptyStateListener = (isEmpty: Boolean) -> Unit
typealias onPagingReachedEndListener = () -> Unit

abstract class AbstractBasePagingAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val lifecycleScope: LifecycleCoroutineScope,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(diffCallback = diffCallback) {

    // Loading State Callback
    private var mLoadingStateListener: OnPagingLoadStateListener? = null

    // Error State Callback
    private var mErrorStateListener: OnPagingErrorStateListener? = null

    // List Empty State Callback
    private var mEmptyStateListener: OnPagingEmptyStateListener? = null

    // End of Pagination Callback
    private var mReachedEndListener: onPagingReachedEndListener? = null

    init {
        addLoadStateListener { loadState -> onLoadStateChanged(loadState) }
    }

    /**
     * 로딩 상태 변화 Listener 설정
     *
     * @param listener Callback Action
     * @author thomas
     * @since 2022/12/12
     **/
    fun setLoadingStateListener(listener: OnPagingLoadStateListener?) {
        mLoadingStateListener = listener
    }

    /**
     * 에러 상태 변화 Listener 설정
     *
     * @param listener Callback Action
     * @author thomas
     * @since 2022/12/12
     **/
    fun setErrorStateListener(listener: OnPagingErrorStateListener?) {
        mErrorStateListener = listener
    }

    /**
     * 페이지 empty 여부 Listener 설정
     *
     * @param listener Callback Action
     * @author thomas
     * @since 2022/12/12
     **/
    fun setEmptyStateListener(listener: OnPagingEmptyStateListener?) {
        mEmptyStateListener = listener
    }

    /**
     * 마지막 페이지 도달 여부 Listener 설정
     *
     * @param listener Callback Action
     * @author thomas
     * @since 2022/12/12
     **/
    fun setReachedEndOfPaginationListener(listener: onPagingReachedEndListener?) {
        mReachedEndListener = listener
    }

//    /**
//     * scrollToTop when LoadState.Refresh
//     *
//     * @author thomas
//     * @since 2022/12/14
//     **/
//    open fun onScrollToTopWhenRefreshed(recyclerView: RecyclerView) {
//        recyclerView.scrollToPosition(0)
//    }
//

    /**
     * RecyclerView.adapter 설정되었을 경우 Callback
     *
     * @author thomas
     * @since 2022/12/13
     **/
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

//        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                super.onItemRangeInserted(positionStart, itemCount)
//
//                if (positionStart == 0) {
//                    recyclerView.scrollToPosition(0)
//                }
//            }
//        })

//        lifecycleScope.launchWhenCreated {
//            loadStateFlow
//                // refresh 요청이 된 경우
//                .distinctUntilChangedBy { it.refresh }
//                // Loading 끝난 경우
//                .filter { !it.isLoading() }
//                // scrollToTop
//                .collect { recyclerView.scrollToPosition(0) }
//        }
    }

    /**
     * Paging 상태 변화에 따른 Callback 처리
     *
     * @author thomas
     * @since 2022/12/12
     **/
    open fun onLoadStateChanged(loadState: CombinedLoadStates) {
        val isLoading = loadState.isLoading()
        mLoadingStateListener?.invoke(isLoading)

        if (isLoading) {
            return
        }

        if (loadState.isError()) {
            mErrorStateListener?.invoke((loadState.refresh as LoadState.Error).error)
            return
        }

        val isEmpty = itemCount <= 0
        mEmptyStateListener?.invoke(isEmpty)

        if (loadState.isReachedEndOfPagination()) {
            mReachedEndListener?.invoke()
        }
    }
}