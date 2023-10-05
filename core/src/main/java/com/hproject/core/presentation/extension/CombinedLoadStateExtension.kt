package com.hproject.core.presentation.extension

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

fun CombinedLoadStates.isLoading() = source.refresh is LoadState.Loading

fun CombinedLoadStates.isError() = source.refresh is LoadState.Error

fun CombinedLoadStates.isDone() = source.refresh is LoadState.NotLoading

fun CombinedLoadStates.isReachedEndOfPagination() = isDone() && source.append.endOfPaginationReached