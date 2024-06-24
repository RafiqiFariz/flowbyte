package com.flowbyte.data.repositories

import android.util.Log
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
    override suspend fun getPlaylist(playlistId: String): Resource<Item> {
        return try {
            val response = playlistApi.getPlaylist(playlistId)
            Log.d("PlaylistRepository", "Fetched playlist: $response")
            Resource.Success(response)
        } catch (e: IOException) {
            Log.e("PlaylistRepository", "IOException: ${e.message}", e)
            Resource.Error(message = "${e.message}")
        } catch (e: HttpException) {
            Log.e("PlaylistRepository", "HttpException: ${e.message}", e)
            Resource.Error(message = e.message())
        } catch (e: Exception) {
            Log.e("PlaylistRepository", "Exception: ${e.message}", e)
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