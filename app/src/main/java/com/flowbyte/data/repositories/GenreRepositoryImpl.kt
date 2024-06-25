package com.flowbyte.data.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.ListArtistBasedByGenreReponse
import com.flowbyte.data.models.albums.AlbumResponse
import com.flowbyte.data.models.albums.NewReleasesResponse
import com.flowbyte.data.models.genres.GenreResponse
import com.flowbyte.data.remote.AlbumApi
import com.flowbyte.data.remote.GenreApi
import com.flowbyte.domain.repositories.AlbumRepository
import com.flowbyte.domain.repositories.GenreRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val genreApi: GenreApi
) : GenreRepository {
    override suspend fun getGenres(): Resource<GenreResponse> {
        return try {
            Resource.Success(genreApi.getGenres())
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: HttpException) {
            Resource.Error(message = e.message())
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }

//    override suspend fun getPlaylistByGenre(): Resource<ListArtistBasedByGenreReponse> {
//        return try {
//            Resource.Success(genreApi.getArtistByGenre())
//        } catch (e: Exception) {
//            Resource.Error(message = "${e.message}")
//        }
//    }
}