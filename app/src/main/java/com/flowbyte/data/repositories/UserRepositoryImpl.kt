package com.flowbyte.data.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse
import com.flowbyte.data.remote.UserApi
import com.flowbyte.domain.repositories.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUserPlaylists(): Resource<FeaturedPlaylistsResponse> {
        return try {
            Resource.Success(userApi.getUserPlaylists())
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}