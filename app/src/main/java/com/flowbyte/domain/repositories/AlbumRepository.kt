package com.flowbyte.domain.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.albums.AlbumResponse
import com.flowbyte.data.models.albums.NewReleasesResponse

interface AlbumRepository {
    suspend fun getNewReleases(): Resource<NewReleasesResponse>
    suspend fun getAlbum(id: String): Resource<AlbumResponse>
}