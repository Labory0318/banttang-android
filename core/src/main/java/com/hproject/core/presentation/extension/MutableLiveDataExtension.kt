package com.hproject.core.presentation.extension

import androidx.lifecycle.MutableLiveData

/**
 * @author hsjun
 * @since 2022/07/05
 */
fun <T> MutableLiveData<T>.mutation(actions: (MutableLiveData<T>) -> Unit) {
    actions(this)
    this.value = this.value
}