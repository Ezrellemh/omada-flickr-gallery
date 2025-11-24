package com.omada.flickrgallery.data.repository

import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.util.Result
import kotlinx.coroutines.delay

class FakePhotoRepository : PhotoRepository {

    override suspend fun getRecentPhotos(page: Int): Result<List<Photo>> {
        delay(500)
        return Result.Success(
            List(20) {
                Photo(
                    id = "recent-$page-$it",
                    title = "Recent Photo $it",
                    imageUrl = "https://placehold.co/300x300?text=Photo+$it"
                )
            }
        )
    }

    override suspend fun searchPhotos(query: String, page: Int): Result<List<Photo>> {
        delay(500)
        return Result.Success(
            List(20) {
                Photo(
                    id = "search-$query-$page-$it",
                    title = "Search ($query) $it",
                    imageUrl = "https://placehold.co/300x300?text=${query}+$it"
                )
            }
        )
    }
}
