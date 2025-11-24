package com.omada.flickrgallery.mvi

import kotlinx.coroutines.CoroutineScope

interface ActionHandler<U : Action> {
    fun onAction(action: U) {}
}

interface ViewModelDelegate<T : ViewState, U : Action, V : Effect> :
    ViewStateHolder<T>,
    ActionHandler<U>,
    EffectDispatcher<V> {

    val initialState: T
    val scope: CoroutineScope
}
