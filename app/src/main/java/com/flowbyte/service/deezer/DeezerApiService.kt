package com.flowbyte.service.deezer
import com.flowbyte.data.GenreResponse
import retrofit2.Call
import retrofit2.http.GET

interface DeezerApiService {
    @GET("genre")
    fun getGenres(): Call<GenreResponse>
}