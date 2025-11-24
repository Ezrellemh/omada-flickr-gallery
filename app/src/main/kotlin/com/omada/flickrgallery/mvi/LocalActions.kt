package com.omada.flickrgallery.mvi

import androidx.compose.runtime.staticCompositionLocalOf

val LocalActions = staticCompositionLocalOf<(Action) -> Unit> { {} }
