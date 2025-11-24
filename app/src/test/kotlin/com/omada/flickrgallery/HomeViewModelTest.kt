package com.omada.flickrgallery

import app.cash.turbine.test
import com.omada.flickrgallery.data.repository.PhotoRepository
import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.ui.home.*
import com.omada.flickrgallery.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModelWithResult(result: Result<List<Photo>>): HomeViewModel {
        val repo = object : PhotoRepository {
            override suspend fun getRecentPhotos(page: Int) = result
            override suspend fun searchPhotos(query: String, page: Int) = result
        }
        return HomeViewModel(repo)
    }

    @Test
    fun loadInitial_success_updatesState() = runTest {
        val photos = listOf(Photo("1", "title", "url"))
        val vm = viewModelWithResult(Result.Success(photos))

        advanceUntilIdle()

        val state = vm.viewState.value
        assertEquals(photos, state.photos)
        assertEquals(false, state.isLoading)
        assertEquals(false, state.isError)
    }

    @Test
    fun loadInitial_error_setsErrorFlag() = runTest {
        val vm = viewModelWithResult(Result.Error(Throwable("fail")))

        advanceUntilIdle()

        val state = vm.viewState.value
        assertTrue(state.isError)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun search_action_triggersSearchAndUpdatesState() = runTest {
        val photos = listOf(Photo("1", "cool", "url"))
        val vm = viewModelWithResult(Result.Success(photos))

        vm.onAction(HomeAction.QueryChanged("cool"))
        vm.onAction(HomeAction.Search)

        advanceUntilIdle()

        val state = vm.viewState.value
        assertEquals(photos, state.photos)
        assertEquals("cool", state.query)
    }

    @Test
    fun photoClicked_emitsNavigationEffect() = runTest {
        val photo = Photo("1", "yooo", "url")
        val vm = viewModelWithResult(Result.Success(listOf(photo)))

        vm.effects.test {
            vm.onAction(HomeAction.PhotoClicked(photo))
            val effect = awaitItem()
            assertEquals(HomeEffect.NavigateToDetail(photo), effect)
        }
    }

    @Test
    fun retry_retriesLastFailedAction() = runTest {
        var callCount = 0

        val repo = object : PhotoRepository {
            override suspend fun getRecentPhotos(page: Int): Result<List<Photo>> {
                callCount++
                return if (callCount == 1) Result.Error(Throwable("fail"))
                else Result.Success(listOf(Photo("2", "success", "url")))
            }

            override suspend fun searchPhotos(query: String, page: Int): Result<List<Photo>> =
                Result.Success(emptyList())
        }

        val vm = HomeViewModel(repo)

        advanceUntilIdle()
        assertTrue(vm.viewState.value.isError)

        vm.onAction(HomeAction.Retry)

        advanceUntilIdle()

        val state = vm.viewState.value
        assertEquals(false, state.isError)
        assertEquals(1, state.photos.size)
    }
}
