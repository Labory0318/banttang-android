package com.hproject.core.presentation.adapter

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Dynamic Type Layout (Grid, Small, Large)
 *
 * @author thomas
 * @since 2022/12/15
 **/
enum class LayoutType {
    GRID, SMALL, LARGE
}

/**
 * Dynamic Layout 지원 PagingAdapter
 *
 * @see AbstractBasePagingAdapter
 * @author thomas
 * @since 2022/12/15
 **/
abstract class AbstractDynamicLayoutPagingAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    lifecycleScope: LifecycleCoroutineScope,
    diffCallback: DiffUtil.ItemCallback<T>,
    private val initialLayout: LayoutType = LayoutType.GRID,
    val gridSpanCount: Int = DEFAULT_GRID_LAYOUT_SPAN_COUNT
) : AbstractBasePagingAdapter<T, VH>(
    lifecycleScope = lifecycleScope,
    diffCallback = diffCallback
) {
    var selectedLayoutType: LayoutType = LayoutType.GRID
        private set

    private var mRecyclerView: RecyclerView? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mSmallLayoutManager: LinearLayoutManager? = null
    private var mLargeLayoutManager: LinearLayoutManager? = null

    /**
     * Layout 변경
     *
     * @author thomas
     * @since 2022/12/15
     **/
    fun setLayoutType(layoutType: LayoutType) {
        val targetLayoutManager = when (layoutType) {
            LayoutType.GRID -> mGridLayoutManager
            LayoutType.SMALL -> mSmallLayoutManager
            LayoutType.LARGE -> mLargeLayoutManager
        }

        if (mRecyclerView?.layoutManager != targetLayoutManager) {
            mRecyclerView?.layoutManager = targetLayoutManager
            selectedLayoutType = layoutType
        }
    }

    /**
     * LayoutManager 생성
     * - Grid, Small, Large 에 해당하는 Layout 생성
     *
     * @author thomas
     * @since 2022/12/15
     **/
    private fun initLayoutManager(context: Context) {
        mGridLayoutManager = createGridLayout(context)
        mSmallLayoutManager = LinearLayoutManager(context)
        mLargeLayoutManager = LinearLayoutManager(context)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
        mRecyclerView?.itemAnimator = null

        // create and initialize layout managers
        initLayoutManager(context = recyclerView.context)

        // setup default layout
        setLayoutType(layoutType = initialLayout)
    }

    /**
     * GridLayoutManger 생성
     * - spanSize to [gridSpanCount]
     * - Header, Footer span size 설정 (Grid 미적용 예외 처리)
     *
     * @author thomas
     * @since 2022/12/14
     **/
    private fun createGridLayout(context: Context): GridLayoutManager {
        return GridLayoutManager(context, gridSpanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return getGridLayoutSpanSize(position = position)
                }
            }
        }
    }

    /**
     * SpanSize 반환
     * - Default is [DEFAULT_GRID_LAYOUT_SPAN_SIZE]
     *
     * @author thomas
     * @since 2022/12/15
     **/
    open fun getGridLayoutSpanSize(position: Int): Int = DEFAULT_GRID_LAYOUT_SPAN_SIZE

    companion object {
        private const val DEFAULT_GRID_LAYOUT_SPAN_COUNT = 2
        private const val DEFAULT_GRID_LAYOUT_SPAN_SIZE = 1
    }
}
