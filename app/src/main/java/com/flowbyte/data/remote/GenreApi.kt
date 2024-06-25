package com.flowbyte.data.remote
import com.flowbyte.data.models.genres.GenreResponse
import com.flowbyte.data.ListArtistBasedByGenreReponse
import retrofit2.http.GET

interface GenreApi {
    @GET("v1/recommendations/available-genre-seeds")
    suspend fun getGenres(): GenreResponse

    @GET("genre/0/artists")
    suspend fun getArtistByGenre(): ListArtistBasedByGenreReponse
}