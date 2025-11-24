package com.omada.flickrgallery.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

interface EffectDispatcher<V : Effect> {
    val effects: SharedFlow<V>
    fun postEffect(effect: V)
}

class DefaultEffectDispatcher<V : Effect>(
    private val scope: CoroutineScope
) : EffectDispatcher<V> {

    private val mutableEffects = MutableSharedFlow<V>()
    override val effects: SharedFlow<V> = mutableEffects.asSharedFlow()

    override fun postEffect(effect: V) {
        scope.launch {
            mutableEffects.emit(effect)
        }
    }
}
