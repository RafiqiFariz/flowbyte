package com.flowbyte.data.remote

import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse
import retrofit2.http.GET

interface UserApi {
    @GET("/v1/me/playlists")
    suspend fun getUserPlaylists(
    ): FeaturedPlaylistsResponse
}