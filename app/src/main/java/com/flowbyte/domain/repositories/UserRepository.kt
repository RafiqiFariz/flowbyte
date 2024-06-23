package com.flowbyte.domain.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse

interface UserRepository {
    suspend fun getUserPlaylists(): Resource<FeaturedPlaylistsResponse>
}