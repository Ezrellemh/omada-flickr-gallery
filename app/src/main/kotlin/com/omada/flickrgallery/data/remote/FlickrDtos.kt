package com.omada.flickrgallery.data.remote

import com.google.gson.annotations.SerializedName
import com.omada.flickrgallery.domain.model.Photo

data class FlickrResponseDto(
    @SerializedName("photos") val photos: PhotosDto,
    @SerializedName("stat") val stat: String,
)

data class PhotosDto(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    @SerializedName("total") val total: String,
    @SerializedName("photo") val photo: List<PhotoDto>,
)

data class PhotoDto(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url_w") val urlW: String? = null,
)

/**
 * Map API model → domain model.
 */
fun PhotoDto.toDomain(): Photo {
    // Use provided url_w when available; otherwise build from components.
    val url = urlW ?: "https://live.staticflickr.com/$server/${id}_${secret}_w.jpg"
    return Photo(
        id = id,
        title = title,
        imageUrl = url,
    )
}
