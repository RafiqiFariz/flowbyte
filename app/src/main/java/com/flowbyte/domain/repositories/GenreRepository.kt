package com.flowbyte.domain.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.ListArtistBasedByGenreReponse
import com.flowbyte.data.models.genres.GenreResponse

interface GenreRepository {
    suspend fun getGenres(): Resource<GenreResponse>
//    suspend fun getPlaylistByGenre(): Resource<ListArtistBasedByGenreReponse>
}