package com.flowbyte.domain.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.Item
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse

interface PlaylistRepository {
    suspend fun getPlaylist(id: String): Resource<Item>
    suspend fun getFeaturedPlaylists(
        limit: Int? = null,
        offset: Int? = null
    ): Resource<FeaturedPlaylistsResponse>
}