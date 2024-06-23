package com.flowbyte.data.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.Item
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse
import com.flowbyte.data.remote.PlaylistApi
import com.flowbyte.domain.repositories.PlaylistRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistApi: PlaylistApi
) : PlaylistRepository {
    override suspend fun getPlaylist(id: String): Resource<Item> {
        return try {
            Resource.Success(playlistApi.getPlaylist((id)))
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun getFeaturedPlaylists(limit: Int?, offset: Int?): Resource<FeaturedPlaylistsResponse> {
        return try {
            Resource.Success(playlistApi.getFeaturedPlaylists(limit, offset))
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: HttpException) {
            Resource.Error(message = e.message())
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}