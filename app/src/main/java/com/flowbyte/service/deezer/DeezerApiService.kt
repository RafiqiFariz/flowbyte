package com.flowbyte.service.deezer
import com.flowbyte.data.GenreResponse
import com.flowbyte.data.ListArtistBasedByGenreReponse
import retrofit2.Call
import retrofit2.http.GET

interface DeezerApiService {
    @GET("genre")
    fun getGenres(): Call<GenreResponse>

    @GET("genre/0/artists")
    fun getArtistByGenre(): Call<ListArtistBasedByGenreReponse>
}