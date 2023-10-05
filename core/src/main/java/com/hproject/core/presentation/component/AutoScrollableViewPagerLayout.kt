package com.hproject.core.presentation.component

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.hproject.core.R


/**
 * 자동 스크롤 ViewPager
 * - [autoScrollTickMillis] 주기로 자동 스크롤 지원
 * - infinite loop scroll 지원
 * - ViewPager2의 경우 final class 선언으로 인해 상속 받을 수 없어 ConstraintLayout 사용하여 구현
 *
 * @author thomas
 * @since 2022/12/01
 **/
open class AutoScrollableViewPagerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_SCROLLING_TICK_MILLIS = 5000L
        const val DEFAULT_SCROLLABLE = true
        const val DEFAULT_INFINITE_LOOP_SCROLLABLE = true
    }

    // 자동 스크롤 대상 ViewPager
    private var pager: ViewPager2? = null

    // 자동 스크롤 활성화 여부
    var isAutoScrollEnabled: Boolean = DEFAULT_SCROLLABLE
        set(value) {
            if (field == value) return

            field = value

            if (!value) {
                stopAutoScrolling()
            }
        }

    // 무한 스크롤 활성화 여부
    var isInfiniteLoopEnabled: Boolean = DEFAULT_INFINITE_LOOP_SCROLLABLE

    // 자동 스크롤 진행중 여부
    var isAutoScrollingActivated = false

    // 스크롤 주기
    private var autoScrollTickMillis: Long = DEFAULT_SCROLLING_TICK_MILLIS
        set(value) {
            if (field == value) return

            field = value
            restartAutoScrolling()
        }

    // 자동 스크롤 Runnable
    private val mAutoScrollingRunnable: Runnable = Runnable { handlePageAutoScrolling() }

    // infinite scrolling 지원을 위한 이전 scroll 상태 값
    private var mPreviousScrollState = ViewPager2.SCROLL_STATE_IDLE

    init {
        initAttributeSet(attrs = attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        pager = children.filterIsInstance(ViewPager2::class.java).firstOrNull()

        if (pager == null) {
            throw NotFoundException("need ViewPager2 for auto scrolling.")
        }

        registerOnPageChangeCallback()
    }

    /**
     * [AutoScrollableViewPagerLayout] attr 초기 설정 값 적용
     *
     * @author thomas
     * @since 2022/12/02
     **/
    private fun initAttributeSet(attrs: AttributeSet?) {
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AutoScrollableViewPagerLayout)

            autoScrollTickMillis = styledAttributes.getInteger(R.styleable.AutoScrollableViewPagerLayout_autoScrollTickMillis, DEFAULT_SCROLLING_TICK_MILLIS.toInt()).toLong()
            isAutoScrollEnabled = styledAttributes.getBoolean(R.styleable.AutoScrollableViewPagerLayout_autoScrollEnabled, DEFAULT_SCROLLABLE)
            isInfiniteLoopEnabled = styledAttributes.getBoolean(R.styleable.AutoScrollableViewPagerLayout_infiniteLoopEnabled, DEFAULT_INFINITE_LOOP_SCROLLABLE)

            styledAttributes.recycle()
        }
    }

    /**
     * 자동 스크롤 시작
     *
     * @author thomas
     * @since 2022/12/01
     **/
    fun startAutoScrolling() {
        if (!isAutoScrollEnabled) return

        isAutoScrollingActivated = true
        postDelayed(mAutoScrollingRunnable, autoScrollTickMillis)
    }

    /**
     * 자동 스크롤 중지
     *
     * @author thomas
     * @since 2022/12/01
     **/
    fun stopAutoScrolling() {
        if (!isAutoScrollEnabled) return

        isAutoScrollingActivated = false
        removeCallbacks(mAutoScrollingRunnable)
    }

    /**
     * 자동 스크롤 재시작
     *
     * @author thomas
     * @since 2022/12/01
     **/
    fun restartAutoScrolling() {
        stopAutoScrolling()
        startAutoScrolling()
    }

    fun moveToNextPage() {
        val pager = pager ?: return
        val adapter = pager.adapter ?: return
        if (adapter.itemCount < 0) return

        val isLastItem = pager.currentItem >= adapter.itemCount - 1
        if (isLastItem && isInfiniteLoopEnabled) {
            pager.currentItem = 0
        }
        else {
            pager.currentItem += 1
        }
    }

    fun moveToPreviousPage() {
        val pager = pager ?: return
        val adapter = pager.adapter ?: return
        if (adapter.itemCount < 0) return

        val isFirstItem = pager.currentItem == 0
        if (isFirstItem && isInfiniteLoopEnabled) {
            pager.currentItem = adapter.itemCount - 1
        }
        else {
            pager.currentItem -= 1
        }
    }

    /**
     * 자동 스크롤 처리
     *
     * @author thomas
     * @since 2022/12/01
     **/
    private fun handlePageAutoScrolling() {
        moveToNextPage()
    }

    /**
     * 무한 스크롤 이벤트 처리
     * - onPageScrollStateChanged callback 처리
     *
     * @author thomas
     * @since 2022/12/02
     **/
    private fun handlePageScrollStateChange(state: Int) {
        val pager = pager ?: return

        if (isInfiniteLoopEnabled) {
            val adapter = pager.adapter
            if (adapter != null && (adapter.itemCount > 1)) { // 최소 2개 인 경우에만 무한 루프 스크롤 진행
                val lastIndex = adapter.itemCount - 1
                val currentIndex = pager.currentItem
                val isOverScroll = (currentIndex >= lastIndex) || (currentIndex <= 0)
                val isScrollStateIdle = (mPreviousScrollState == ViewPager2.SCROLL_STATE_DRAGGING) && (state == ViewPager2.SCROLL_STATE_IDLE)

                if (isOverScroll && isScrollStateIdle) {
                    if (currentIndex == lastIndex) {
                        moveToNextPage()
                    }
                    else {
                        moveToPreviousPage()
                    }
                }
            }
        }

        mPreviousScrollState = state
    }

    /**
     * ViewPager Listener 등록
     * - onPageScrollStateChanged:  무한 스크롤 이벤트 처리
     * - onPageSelected:            자동 스크롤 처리 (중복 처리를 막기 위해 이전 요청이 취소 되고 재시작 된다)
     *
     * @author thomas
     * @since 2022/12/01
     **/
    private fun registerOnPageChangeCallback() {
        val pager = pager ?: return

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                handlePageScrollStateChange(state = state)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                restartAutoScrolling()
            }
        })
    }
}