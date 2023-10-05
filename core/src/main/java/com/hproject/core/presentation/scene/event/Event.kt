package com.hproject.core.presentation.scene.event

/**
 * ViewModel Event
 * - ViewModel > UIController(Activity or Fragment) UI Event 정보
 *
 * @param content Event 내용
 * @author thomas
 * @since 2022/11/03
 **/
open class Event<out T>(private val content: T) {
    // 중복 처리 방지를 위한 Event 처리 여부
    var hasBeenHandled: Boolean = false
        private set

    /**
     * Event [content] 정보
     * - Event 처리 후 중복 처리를 방지하기 위한 목적으로 사용.
     * - 1회 처리 후, 이후로는 null 값 반환 됨.
     * - [content] 원본 값은 [peekContent] 참조.
     *
     * @return [content]
     * @author thomas
     * @since 2022/11/03
     **/
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Event [content] 정보
     *
     * @return [content]
     * @author thomas
     * @since 2022/11/03
     **/
    open fun peekContent(): T = content
}