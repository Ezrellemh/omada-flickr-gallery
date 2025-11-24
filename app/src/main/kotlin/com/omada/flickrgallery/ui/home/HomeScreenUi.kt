package com.omada.flickrgallery.ui.home

import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.mvi.Action
import com.omada.flickrgallery.mvi.Effect
import com.omada.flickrgallery.mvi.ViewState

data class HomeState(
    val query: String = "",
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isPaginating: Boolean = false,
    val endReached: Boolean = false,
    val isEmpty: Boolean = false
) : ViewState

sealed interface HomeAction : Action {
    data object LoadInitial : HomeAction
    data class QueryChanged(val query: String) : HomeAction
    data object Search : HomeAction
    data object LoadNextPage : HomeAction
    data object Retry : HomeAction
    data class PhotoClicked(val photo: Photo) : HomeAction
}

sealed interface HomeEffect : Effect {
    data class NavigateToDetail(val photo: Photo) : HomeEffect
}
