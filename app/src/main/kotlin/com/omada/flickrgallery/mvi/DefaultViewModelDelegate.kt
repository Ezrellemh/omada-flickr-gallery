package com.omada.flickrgallery.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultViewModelDelegate<T : ViewState, U : Action, V : Effect>(
    override val initialState: T,
    override val scope: CoroutineScope,
    private val viewStateHolder: ViewStateHolder<T> = DefaultViewStateHolder(initialState),
    private val effectDispatcher: EffectDispatcher<V>
) : ViewModelDelegate<T, U, V> {

    override val viewState: StateFlow<T>
        get() = viewStateHolder.viewState

    override val effects: SharedFlow<V>
        get() = effectDispatcher.effects

    override fun reduce(reducer: StateReducer<T>) {
        viewStateHolder.reduce(reducer)
    }

    override fun postEffect(effect: V) {
        effectDispatcher.postEffect(effect)
    }

    // onAction stays no-op here; each ViewModel overrides it.
}
