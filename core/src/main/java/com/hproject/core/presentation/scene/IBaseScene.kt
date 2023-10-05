package com.hproject.core.presentation.scene

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

typealias Inflate<VDB> = (LayoutInflater, ViewGroup?, Boolean) -> VDB

interface IBaseScene<VDB : ViewDataBinding, VM : ViewModel> {
    val binding: VDB
    val viewModel: VM

    fun onInitView()
    fun onRegisterViewModelObserver()
    fun onReceivedViewModelEvent(event: Any)
    fun onRegisterResultObserver()
}