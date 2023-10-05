package com.hproject.core.presentation.scene

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hproject.core.extension.TAG
import com.hproject.core.presentation.scene.event.EventBus
import timber.log.Timber

/**
 * BaseViewModel
 *
 * @author thomas
 * @since 2022/11/03
 **/
abstract class AbstractBaseViewModel : ViewModel() {
    sealed class Event{
        object NavigateUp : Event()
        object ShowScreenBlockProgress : Event()
        object HideScreenBlockProgress : Event()
        data class ShowToastMessage(
            @StringRes val messageRes: Int? = null,
            val message: String? = null
        ) : Event()
    }


    private val eventBus = EventBus()
    val eventMessage get() = eventBus.eventMessage

    init {
        Timber.i(this.TAG)
    }

    override fun onCleared() {
        super.onCleared()

        Timber.i(this.TAG)
    }

    open fun sendEventToView(
        event: Any,
        delay: Long = 0
    ) = eventBus.sendEventToView(
        event = event,
        delay = delay
    )

    /**
     * 토스트 출력 이벤트 전달
     *
     * @param message   출력할 토스트 메세지
     * @param delay     지연 시간 (단위: ms)
     * @see EventBus
     * @author thomas
     * @since 2022/11/03
     **/
    fun sendShowToastEvent(
        @StringRes message: Int,
        delay: Long = 0L
    ) = sendEventToView(
        event = Event.ShowToastMessage(messageRes = message),
        delay = delay
    )

    /**
     * 토스트 출력 이벤트 전달
     *
     * @param message   출력할 토스트 메세지
     * @param delay     지연 시간 (단위: ms)
     * @see EventBus
     * @author thomas
     * @since 2022/11/03
     **/
    fun sendShowToastEvent(
        message: String,
        delay: Long = 0L
    ) = sendEventToView(
        event = Event.ShowToastMessage(message = message),
        delay = delay
    )


    /**
     * screen block progress 표출
     *
     * @author thomas
     * @since 2023/01/02
     **/
    fun showScreenBlockProgress(
        delay: Long = 0
    ) = sendEventToView(
        event = Event.ShowScreenBlockProgress,
        delay = delay
    )

    /**
     * screen block progress 숨김
     *
     * @author thomas
     * @since 2023/01/02
     **/
    fun hideScreenBlockProgress(
        delay: Long = 0
    ) = sendEventToView(
        event = Event.HideScreenBlockProgress,
        delay = delay
    )

    fun navigateUp(
        delay: Long = 0
    ) = sendEventToView(
        event = Event.NavigateUp,
        delay = delay
    )

    /**
     * [AbstractBaseViewModel] Base Factory
     * - [ViewModel]의 경우 ComponentActivity 하위의 ViewModel.Provider 에 의해 생성 되기 때문에 new ViewModel() 로 생성할 수 없다.
     * 따라서, [ViewModelProvider.Factory]를 통해 생성해야 하며, parameter 를 전달하기 위해서는 해당 Factory 생성자를 통해 전달해야 한다.
     *
     * @author thomas
     * @since 2022/11/07
     **/
    abstract class Factory<T> : ViewModelProvider.Factory {
        abstract fun getViewModel(): T

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(modelClass)) {
                getViewModel() as T
            }
            else {
                throw IllegalArgumentException()
            }
        }
    }
}