package com.flowbyte.data.remote

import com.flowbyte.data.models.playlist.Item
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaylistApi {
    @GET("v1/playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Path("playlist_id") playlistId: String
    ): Item

    @GET("v1/browse/featured-playlists")
    suspend fun getFeaturedPlaylists(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): FeaturedPlaylistsResponse
}