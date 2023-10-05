package com.hproject.core.presentation.scene.event

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

/**
 * ViewModel > UIController Event 전달 주체
 *
 * @author thomas
 * @since 2022/11/03
 **/
open class EventBus {
    companion object {
        private const val HANDLE_WHAT_NOTIFY_TO_VIEW_DEFAULT = 0
    }

    // Event
    private val _eventMessage: MutableLiveData<Event<Any>> = MutableLiveData()
    val eventMessage: LiveData<Event<Any>> get() = _eventMessage

    // UI Handler
    private val mHandlerMainForUi = Handler(Looper.getMainLooper()) {
        when (it.what) {
            HANDLE_WHAT_NOTIFY_TO_VIEW_DEFAULT -> handleSendEventToView(it)
            else -> onReceivedHandlerEvent(it)
        }
        true
    }

    open fun sendEventToView(
        what: Int = HANDLE_WHAT_NOTIFY_TO_VIEW_DEFAULT,
        event: Any,
        delay: Long = 0
    ) {
        Timber.i("what=$what, event=${event}, delay=$delay")

        mHandlerMainForUi.sendMessageDelayed(
            Message.obtain().also {
                it.what = what
                it.obj = event
            },
            delay
        )
    }

    /**
     * [mHandlerMainForUi] event callback 처리
     * - event callback 전달 받은 후 [eventMessage] 정보 업데이트
     *
     * @param message   수신 메세지 정보
     * @author thomas
     * @since 2022/11/03
     **/
    open fun handleSendEventToView(message: Message) {
        Timber.i("what=${message.what}, obj=${message.obj}")

        message.obj?.let { obj ->
            _eventMessage.value = Event(obj)
        }
    }

    open fun onReceivedHandlerEvent(message: Message) {
        Timber.i("what=${message.what}, obj=${message.obj}")
    }
}