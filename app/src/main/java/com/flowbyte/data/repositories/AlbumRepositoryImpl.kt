package com.flowbyte.data.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.albums.AlbumResponse
import com.flowbyte.data.models.albums.NewReleasesResponse
import com.flowbyte.data.remote.AlbumApi
import com.flowbyte.domain.repositories.AlbumRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumApi: AlbumApi
) : AlbumRepository {
    override suspend fun getNewReleases(): Resource<NewReleasesResponse> {
        return try {
            Resource.Success(albumApi.getNewReleases())
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: HttpException) {
            Resource.Error(message = e.message())
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun getAlbum(id: String): Resource<AlbumResponse> {
        return try {
            Resource.Success(albumApi.getAlbum((id)))
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}