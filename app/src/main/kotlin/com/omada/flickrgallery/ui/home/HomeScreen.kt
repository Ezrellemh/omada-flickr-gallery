package com.omada.flickrgallery.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.omada.flickrgallery.R
import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.mvi.LocalActions
import com.omada.flickrgallery.mvi.StateHostScreen
import com.omada.flickrgallery.ui.ErrorScreen

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (Photo) -> Unit = {}
) {
    StateHostScreen(
        viewStateFlow = viewModel.viewState,
        effectsFlow = viewModel.effects,
        onAction = viewModel::onAction,
        onEffect = { effect ->
            when (effect) {
                is HomeEffect.NavigateToDetail -> onNavigateToDetail(effect.photo)
            }
        }
    ) { state ->
        val onAction = LocalActions.current

        when {
            state.isLoading && state.photos.isEmpty() ->
                LoadingSpinner()

            state.isError && state.photos.isEmpty() ->
                ErrorScreen(onRetry = { onAction(HomeAction.Retry) })

            else ->
                HomeContent(
                    state = state,
                    onAction = onAction
                )
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        SearchBar(
            query = state.query,
            onQueryChange = { onAction(HomeAction.QueryChanged(it)) },
            onSearch = { onAction(HomeAction.Search) }
        )

        Spacer(Modifier.height(12.dp))

        when {
            state.isEmpty ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_photos_found),
                        style = LocalTextStyle.current,
                        color = Color.Black
                    )
                }

            else ->
                PhotoGrid(
                    photos = state.photos,
                    isPaginating = state.isPaginating,
                    onLoadMore = {
                        if (!state.endReached && !state.isPaginating && !state.isLoading) {
                            onAction(HomeAction.LoadNextPage)
                        }
                    },
                    onPhotoClick = { onAction(HomeAction.PhotoClicked(it)) }
                )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.search_photos), color = Color.Black) },
        singleLine = true,
        trailingIcon = {
            TextButton(onClick = onSearch) {
                Text(stringResource(R.string.search), color = Color.Black)
            }
        },
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
    )
}

@Composable
private fun PhotoGrid(
    photos: List<Photo>,
    isPaginating: Boolean,
    onLoadMore: () -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(photos) { index, photo ->
            if (index == photos.lastIndex - 5) onLoadMore()

            Image(
                painter = rememberAsyncImagePainter(photo.imageUrl),
                contentDescription = photo.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clickable { onPhotoClick(photo) }
            )
        }

        if (isPaginating) {
            item(span = { GridItemSpan(3) }) {
                InlineLoadingSpinner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingSpinner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun InlineLoadingSpinner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
