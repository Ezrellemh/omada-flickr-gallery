package com.omada.flickrgallery.data.repository

import com.omada.flickrgallery.data.remote.FlickrApi
import com.omada.flickrgallery.data.remote.toDomain
import com.omada.flickrgallery.domain.model.Photo
import com.omada.flickrgallery.util.Result
import javax.inject.Inject
import javax.inject.Named

class PhotoRepositoryImpl @Inject constructor(
    private val api: FlickrApi,
    @Named("apiKey") private val apiKey: String
) : PhotoRepository {

    override suspend fun getRecentPhotos(page: Int): Result<List<Photo>> {
        return try {
            val response = api.getRecentPhotos(apiKey = apiKey, page = page)
            val items = response.photos.photo.map { it.toDomain() }
            Result.Success(items)
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }

    override suspend fun searchPhotos(query: String, page: Int): Result<List<Photo>> {
        return try {
            val response = api.searchPhotos(apiKey = apiKey, query = query, page = page)
            val items = response.photos.photo.map { it.toDomain() }
            Result.Success(items)
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}
