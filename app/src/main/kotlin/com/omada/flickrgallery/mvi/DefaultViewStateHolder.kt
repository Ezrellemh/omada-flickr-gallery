package com.omada.flickrgallery.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

typealias StateReducer<T> = T.() -> T

interface ViewStateHolder<T : ViewState> {
    val viewState: StateFlow<T>
    fun reduce(reducer: StateReducer<T>)
}

class DefaultViewStateHolder<T : ViewState>(
    initialState: T
) : ViewStateHolder<T> {

    private val mutableState = MutableStateFlow(initialState)

    override val viewState: StateFlow<T>
        get() = mutableState.asStateFlow()

    override fun reduce(reducer: StateReducer<T>) {
        mutableState.update { it.reducer() }
    }
}
