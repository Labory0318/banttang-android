package com.hproject.core.presentation.binding_adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("isVisible")
    fun View.isVisible(value: Boolean) {
        isVisible = value
    }

    @JvmStatic
    @BindingAdapter("isSelected")
    fun View.isSelected(value: Boolean) {
        isSelected = value
    }
}