package com.omada.flickrgallery.data.repository

import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.util.Result

interface PhotoRepository {
    suspend fun getRecentPhotos(page: Int): Result<List<Photo>>
    suspend fun searchPhotos(query: String, page: Int): Result<List<Photo>>
}
