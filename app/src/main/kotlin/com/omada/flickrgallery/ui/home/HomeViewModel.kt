package com.omada.flickrgallery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omada.flickrgallery.data.repository.PhotoRepository
import com.omada.flickrgallery.mvi.DefaultEffectDispatcher
import com.omada.flickrgallery.mvi.DefaultViewModelDelegate
import com.omada.flickrgallery.mvi.StateReducer
import com.omada.flickrgallery.mvi.ViewModelDelegate
import com.omada.flickrgallery.util.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PhotoRepository,
) : ViewModel(),
    ViewModelDelegate<HomeState, HomeAction, HomeEffect> {

    private val delegate = DefaultViewModelDelegate<HomeState, HomeAction, HomeEffect>(
        initialState = HomeState(),
        scope = viewModelScope,
        effectDispatcher = DefaultEffectDispatcher(viewModelScope)
    )

    override val initialState: HomeState
        get() = delegate.initialState

    override val scope = viewModelScope

    override val viewState
        get() = delegate.viewState

    override val effects
        get() = delegate.effects

    override fun reduce(reducer: StateReducer<HomeState>) {
        delegate.reduce(reducer)
    }

    override fun postEffect(effect: HomeEffect) {
        delegate.postEffect(effect)
    }

    private var currentPage = 1
    private var currentQuery: String? = null
    private var currentJob: Job? = null
    private var lastFailedAction: HomeAction? = null

    init {
        onAction(HomeAction.LoadInitial)
    }

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.LoadInitial -> loadInitial()
            is HomeAction.QueryChanged -> reduce { copy(query = action.query) }
            is HomeAction.Search -> onSearch()
            is HomeAction.LoadNextPage -> loadNextPage()
            is HomeAction.Retry -> retry()
            is HomeAction.PhotoClicked -> postEffect(HomeEffect.NavigateToDetail(action.photo))
        }
    }

    private fun onSearch() {
        currentQuery = viewState.value.query.takeIf { it.isNotBlank() }
        resetPaging()
        loadPage(isInitial = true)
    }

    private fun loadInitial() {
        currentQuery = null
        resetPaging()
        loadPage(isInitial = true)
    }

    private fun loadNextPage() {
        val s = viewState.value
        if (s.endReached || s.isPaginating) return
        currentPage += 1
        loadPage(isInitial = false)
    }

    private fun retry() {
        val failed = lastFailedAction ?: HomeAction.LoadInitial
        lastFailedAction = null
        onAction(failed)
    }

    private fun resetPaging() {
        currentPage = 1
        reduce {
            copy(
                photos = emptyList(),
                endReached = false,
                isEmpty = false
            )
        }
    }

    private fun loadPage(isInitial: Boolean) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            if (isInitial) {
                reduce { copy(isLoading = true, isPaginating = false) }
            } else {
                reduce { copy(isPaginating = true) }
            }

            val result = if (currentQuery == null) {
                repository.getRecentPhotos(page = currentPage)
            } else {
                repository.searchPhotos(query = currentQuery!!, page = currentPage)
            }

            when (result) {
                is Result.Success -> {
                    val newPhotos = result.data
                    reduce {
                        val merged = if (isInitial) newPhotos else photos + newPhotos
                        copy(
                            photos = merged,
                            isLoading = false,
                            isPaginating = false,
                            endReached = newPhotos.isEmpty(),
                            isEmpty = merged.isEmpty(),
                            isError = false,
                        )
                    }
                }
                is Result.Error -> {
                    lastFailedAction = if (isInitial) {
                        if (currentQuery == null) HomeAction.LoadInitial else HomeAction.Search
                    } else {
                        HomeAction.LoadNextPage
                    }

                    reduce {
                        copy(
                            isLoading = false,
                            isPaginating = false,
                            isError = true
                        )
                    }
                }
            }
        }
    }
}
