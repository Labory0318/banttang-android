package com.hproject.core.presentation.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractBaseListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    abstract inner class AbstractBaseViewHolder<T, VDB : ViewDataBinding>(
        val binding: VDB
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun onBindViewHolder(item: T)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AbstractBaseViewHolder<T, *>)?.onBindViewHolder(getItem(position))
    }
}