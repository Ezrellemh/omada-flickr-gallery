package com.omada.flickrgallery.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T : ViewState, U : Action, V : Effect> StateHostScreen(
    viewStateFlow: StateFlow<T>,
    effectsFlow: SharedFlow<V>,
    onAction: (U) -> Unit,
    onEffect: (V) -> Unit,
    content: @Composable (T) -> Unit
) {
    val viewState = viewStateFlow.collectAsStateWithLifecycle().value

    CompositionLocalProvider(
        LocalActions provides (onAction as (Action) -> Unit)
    ) {
        content(viewState)
    }

    LaunchedEffect(Unit) {
        effectsFlow.collect { effect -> onEffect(effect) }
    }
}
