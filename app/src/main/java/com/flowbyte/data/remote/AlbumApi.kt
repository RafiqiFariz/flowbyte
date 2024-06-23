package com.flowbyte.data.remote

import com.flowbyte.data.models.albums.AlbumResponse
import com.flowbyte.data.models.albums.NewReleasesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumApi {
    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(): NewReleasesResponse

    @GET("v1/albums/{id}")
    suspend fun getAlbum(
        @Path("id") albumId: String
    ): AlbumResponse
}